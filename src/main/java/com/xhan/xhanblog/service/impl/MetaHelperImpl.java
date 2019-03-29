package com.xhan.xhanblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dao.TArticle;
import com.xhan.xhanblog.entity.dao.TArticleExample;
import com.xhan.xhanblog.entity.dao.TMeta;
import com.xhan.xhanblog.entity.dao.TMetaExample;
import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.dto.UpdateCateDTO;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.MetaVO;
import com.xhan.xhanblog.exception.meta.MetaException;
import com.xhan.xhanblog.exception.meta.MetaTypeNotSuitException;
import com.xhan.xhanblog.exception.meta.cate.*;
import com.xhan.xhanblog.repository.TArticleMapper;
import com.xhan.xhanblog.repository.TMetaMapper;
import com.xhan.xhanblog.service.MetaHelper;
import com.xhan.xhanblog.util.MetaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Service
public class MetaHelperImpl implements MetaHelper {

    private final TMetaMapper metaMapper;
    private final TArticleMapper articleMapper;

    @Autowired
    public MetaHelperImpl(TMetaMapper metaMapper, TArticleMapper articleMapper) {
        this.metaMapper = metaMapper;
        this.articleMapper = articleMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetaVO> getAllCategories() {
        TMetaExample example = new TMetaExample();
        example.createCriteria().andMTypeEqualTo(MetaType.CATEGORY.getType());

        return metaMapper.selectByExample(example).stream()
                .map(MetaVO::new)
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetaVO> getAllTags() {
        TMetaExample example = new TMetaExample();
        example.createCriteria().andMTypeEqualTo(MetaType.TAG.getType());

        return metaMapper.selectByExample(example).stream()
                .map(MetaVO::new)
                .collect(toList());
    }

    @Override
    @Transactional(rollbackFor = CategoryException.class)
    public void deleteCate(String delete) {
        // content和type上有联合unique约束
        Long deleteId = metaMapper.getCateIdByContent(delete);
        if (deleteId == null)
            throw new CategoryNotFoundException();
        metaMapper.deleteByPrimaryKey(deleteId);

        TArticleExample articleExample = new TArticleExample();
        articleExample.createCriteria().andCategoryIdEqualTo(deleteId);
        articleMapper.selectByExample(articleExample).forEach(a -> {
            a.setCategoryId(null);
            articleMapper.updateByPrimaryKey(a);
        });
    }

    @Override
    @Transactional(rollbackFor = CategoryException.class)
    public void createCategory(CreateCateDTO create) {
        // 需要查看parent和content 是否已经存在
        TMeta cate = new TMeta(create);
        checkCCateDTO(create);

        TMetaExample example = new TMetaExample();
        example.createCriteria()
                .andParentEqualTo(create.getParent())
                .andMContentEqualTo(create.getContent());

        if (metaMapper.countByExample(example) > 0)
            throw new CateAlreadyExistException();

        metaMapper.insert(cate);
    }

    @Override
    @Transactional(rollbackFor = MetaException.class)
    public void updateCategory(@Valid UpdateCateDTO update) {
        TMeta cate = metaMapper.selectByPrimaryKey(update.getCId());
        checkUCateDTO(update, cate);

        if (!cate.getMContent().equals(update.getContent()) && update.getContent() != null) {
            // 如果修改了content,就要修改TArticle中对应的属性
            articleMapper.updateCategoryContent(update.getContent(), cate.getMId());
        }
        TMeta nCate = copyAttr(update);
        nCate.setMId(cate.getMId());
        if (metaMapper.updateByPrimaryKeySelective(nCate) != 1)
            throw new CategoryException("error in change attr");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleVO> getArticleByCate(String cate, Integer page, Integer pageSize) {
        if (page < 0 || pageSize < 0)
            throw new IllegalArgumentException();

        TMetaExample metaExample = new TMetaExample();
        metaExample.createCriteria()
                .andMTypeEqualTo(MetaType.CATEGORY.getType())
                .andMContentEqualTo(cate);

        // content 上有unique约束
        if (metaMapper.countByExample(metaExample) != 1)
            throw new CategoryNotFoundException();

        TArticleExample example = new TArticleExample();
        example.createCriteria().andCategoryEqualTo(cate);

        PageHelper.startPage(page, pageSize);
        List<TArticle> articles = articleMapper.selectByExample(example);

        return articles.stream()
                .filter(article -> article.getStatus().equals(Status.PUBLISHED.getStatus()))
                .map(ArticleVO::new)
                .collect(toList());
    }

    private TMeta copyAttr(@Valid UpdateCateDTO update) {
        TMeta cate = new TMeta();
        if (hasText(update.getContent()))
            cate.setMContent(update.getContent());
        if (hasText(update.getDescription()))
            cate.setDescription(update.getDescription());
        if (update.getParent() != null)
            cate.setParent(update.getParent());
        return cate;
    }

    private void checkUCateDTO(@Valid UpdateCateDTO update, TMeta cate) {
        if (!update.isUpdateTextLegal())
            throw new UpdateCateDTOIllegalException();
        // 如果对应id 没有TMeta或者update中设置了父id，而父id不存在则抛出异常
        if (cate == null)
            throw new CategoryNotFoundException();
        if (!cate.getMType().equals(MetaType.CATEGORY.getType()))
            throw new MetaTypeNotSuitException();
        if (update.getParent() != null && isCateExistById(update.getParent()))
            throw new CategoryNotFoundException();
    }

    private boolean isCateExistById(Long cateId) {
        return metaMapper.selectByPrimaryKey(cateId) == null;
    }

    private void checkCCateDTO(@Valid CreateCateDTO create) {
        if (!create.isCreateTextLegal())
            throw new CreateCateDTOIllegalException();
        if (create.getParent() != null && isCateExistById(create.getParent()))
            throw new CategoryNotFoundException();
    }

}
