package com.xhan.xhanblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.xhan.xhanblog.entity.dao.*;
import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.dto.UpdateCateDTO;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.MetaVO;
import com.xhan.xhanblog.exception.meta.MetaException;
import com.xhan.xhanblog.exception.meta.MetaTypeNotSuitException;
import com.xhan.xhanblog.exception.meta.cate.*;
import com.xhan.xhanblog.exception.meta.tag.TagException;
import com.xhan.xhanblog.repository.TArticleMapper;
import com.xhan.xhanblog.repository.TMetaMapper;
import com.xhan.xhanblog.repository.TRelationMapper;
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
    private final TRelationMapper relationMapper;

    @Autowired
    public MetaHelperImpl(TMetaMapper metaMapper, TArticleMapper articleMapper, TRelationMapper relationMapper) {
        this.metaMapper = metaMapper;
        this.articleMapper = articleMapper;
        this.relationMapper = relationMapper;
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
    public void deleteCate(String content) {
        // clear meta table
        Long deleteId = metaMapper.getCateIdByContent(content);
        if (deleteId == null)
            throw new CategoryNotFoundException();
        metaMapper.deleteByPrimaryKey(deleteId);
        // clear cate in article
        articleMapper.setCateToNull(deleteId);
        // clear relation
        TRelationExample example = new TRelationExample();
        example.createCriteria().andMIdEqualTo(deleteId);
        relationMapper.deleteByExample(example);
    }

    @Override
    @Transactional(rollbackFor = CategoryException.class)
    public void createCategory(CreateCateDTO create) {
        // 需要查看content 是否已经存在（在有unique约束的情况下是否还需要检查）
        TMeta cate = new TMeta(create);
        if (!create.isCreateTextLegal())
            throw new CreateCateDTOIllegalException();

        if (metaMapper.getCateIdByContent(create.getContent()) != null)
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
        TMeta nCate = copyAttr(update, cate);
        if (metaMapper.updateByPrimaryKeySelective(nCate) != 1)
            throw new CategoryException("error in change attr");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticleVO> getArticleByCateId(Long cateId, Integer page, Integer pageSize, boolean isAdmin) {
        TArticleExample example = new TArticleExample();
        example.createCriteria().andCategoryIdEqualTo(cateId);
        example.setOrderByClause("create_time DESC");

        return veilArticle(page, pageSize, isAdmin, example);
    }

    private List<ArticleVO> veilArticle(Integer page, Integer pageSize, boolean isAdmin, TArticleExample example) {
        PageHelper.startPage(page, pageSize);
        List<TArticle> articles = articleMapper.selectByExample(example);

        return veilArticle(isAdmin, articles);
    }

    @Override
    public List<ArticleVO> getArticleByTag(String tag, Integer page, Integer pageSize, boolean isAdmin) {
        Long tagId = getTagIdByContent(tag);

        PageHelper.startPage(page, pageSize);
        List<TArticle> articles = articleMapper.selectByTagId(tagId);
        return veilArticle(isAdmin, articles);
    }

    private List<ArticleVO> veilArticle(boolean isAdmin, List<TArticle> articles) {
        return articles.stream()
                .filter(a -> !isAdmin && a.isBanished())
                .map(ArticleVO::new)
                .collect(toList());
    }

    private Long getTagIdByContent(String tag) {
        TMetaExample example = new TMetaExample();
        example.createCriteria()
                .andMTypeEqualTo(MetaType.TAG.getType())
                .andMContentEqualTo(tag);

        List<TMeta> metas = metaMapper.selectByExample(example);
        if (metas.size() != 1)
            throw new TagException();
        return metas.get(0).getMId();
    }

    private TMeta copyAttr(@Valid UpdateCateDTO update, TMeta c) {
        TMeta cate = new TMeta();
        cate.setMId(c.getMId());
        if (hasText(update.getContent()))
            cate.setMContent(update.getContent());
        if (hasText(update.getDescription()))
            cate.setDescription(update.getDescription());
        return cate;
    }

    private void checkUCateDTO(@Valid UpdateCateDTO update, TMeta cate) {
        if (!update.isUpdateTextLegal())
            throw new UpdateCateDTOIllegalException();
        // 如果对应id 没有TMeta或者update中设置了父id，而父id不存在则抛出异常
        if (cate == null)
            throw new CategoryNotFoundException();
        if (cate.isTag())
            throw new MetaTypeNotSuitException();
    }

}
