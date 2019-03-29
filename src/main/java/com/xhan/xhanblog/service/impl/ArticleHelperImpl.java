package com.xhan.xhanblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dao.*;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.UploadArtVO;
import com.xhan.xhanblog.exception.DeleteRecordException;
import com.xhan.xhanblog.exception.UpdateFailException;
import com.xhan.xhanblog.exception.article.ArticleNotFoundException;
import com.xhan.xhanblog.exception.article.ArticleSaveException;
import com.xhan.xhanblog.exception.article.ContentTooLongException;
import com.xhan.xhanblog.exception.meta.cate.CategoryNotFoundException;
import com.xhan.xhanblog.exception.meta.tag.TagException;
import com.xhan.xhanblog.repository.TArticleMapper;
import com.xhan.xhanblog.repository.TMetaMapper;
import com.xhan.xhanblog.repository.TRelationMapper;
import com.xhan.xhanblog.service.ArticleHelper;
import com.xhan.xhanblog.util.MetaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleHelperImpl implements ArticleHelper {

    private final TRelationMapper relationMapper;
    private final TArticleMapper articleMapper;
    private final TMetaMapper metaMapper;

    @Autowired
    public ArticleHelperImpl(TRelationMapper relationMapper, TArticleMapper articleMapper, TMetaMapper metaMapper) {
        this.relationMapper = relationMapper;
        this.articleMapper = articleMapper;
        this.metaMapper = metaMapper;
    }

    @Override
    public List<ArticleVO> getArticleList(boolean isAdmin) {
        return getArticleList(0, ArticleHelper.DEFAULT_PAGE_SIZE, isAdmin);
    }

    @Override
    public List<ArticleVO> getArticleList(int page, int pageSize, boolean isAdmin) {
        TArticleExample example = new TArticleExample();
        if (!isAdmin){
            example.createCriteria().andStatusEqualTo(Status.PUBLISHED.getStatus());
        }
        PageHelper.startPage(page, pageSize);
        List<TArticle> articles = articleMapper.selectByExample(example);

        return articles.stream()
                .map(ArticleVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 保存一个UploadArtVO（cate和tag可能为null）
     *
     * @param artVO UploadArtVO
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void save(@Valid UploadArtVO artVO) {
        if (!artVO.isTextOverflow())
            throw new ContentTooLongException();

        TArticle article = new TArticle(artVO);
        if (articleMapper.insert(article) != 1)
            throw new ArticleSaveException();
        saveCateRelation(article);
        saveTagAndRelations(article);
    }

    @Override
    @Transactional(rollbackFor = ArticleNotFoundException.class)
    public TArticle getArticleById(Long aId) {
        TArticleExample example = new TArticleExample();
        example.createCriteria().andAIdEqualTo(aId);

        if (articleMapper.selectByExampleWithBLOBs(example).size() == 1) {
            TArticle article = articleMapper.selectByExampleWithBLOBs(example).get(0);
            articleMapper.updateByPrimaryKeySelective(new TArticle(aId, article.getHits() + 1));
            return article;
        } else {
            throw new ArticleNotFoundException();
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(Long delete) {
        if (!artIdExist(delete))
            throw new ArticleNotFoundException();
        // 这是完全没有orm的写法啊
        TArticle article = articleMapper.selectByPrimaryKey(delete);
        TMetaExample metaExample = new TMetaExample();
        List<Long> metaIds = getTagIdsFromTArticle(article, metaExample);

        if (article.getCategoryId() != null) {
            metaIds.add(article.getCategoryId());
        }
        metaExample.clear();
        TRelationExample tExample = new TRelationExample();
        metaIds.forEach(id -> deleteRelationAndUselessTags(delete, metaExample, tExample, id));

        isDeleteSuccess(articleMapper.deleteByPrimaryKey(delete));
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void banishById(Long delete) {
        if (!artIdExist(delete))
            throw new ArticleNotFoundException();
        TArticle article = articleMapper.selectByPrimaryKey(delete);
        if (article.getStatus().equals(Status.BANISHED.getStatus())) {
            deleteById(delete);
            return;
        }
        TArticle banish = new TArticle(article.getAId(), Status.BANISHED.getStatus());
        if (articleMapper.updateByPrimaryKeySelective(banish) != 1)
            throw new UpdateFailException();
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void recoverById(Long recover) {
        if (!artIdExist(recover))
            throw new ArticleNotFoundException();
        TArticle article = articleMapper.selectByPrimaryKey(recover);
        if (article.getStatus().equals(Status.PUBLISHED.getStatus())) {
            return;
        }
        TArticle reco = new TArticle(article.getAId(), Status.PUBLISHED.getStatus());
        if (articleMapper.updateByPrimaryKeySelective(reco) != 1)
            throw new UpdateFailException();
    }

    /**
     * 从TArticle对象中得到tags并且将其分离得到tag列表，再得到每一个tag对应的TMeta id
     * 当该对象的tags属性为null时，返回一个空列表
     *
     * @param article     TArticle 对象，已经检验过不会为null
     * @param metaExample TMetaExample
     * @return tags主键组成的list
     */
    private List<Long> getTagIdsFromTArticle(TArticle article, TMetaExample metaExample) {
        return article.getTags() == null
                ? new LinkedList<>()
                : splitTags(article.getTags()).stream()
                .map(tag -> fromContentGetMetaId(metaExample, tag, MetaType.TAG.getType()))
                .collect(Collectors.toList());
    }

    /**
     * 这个函数的作用是在删除一个文章前，先从关系表中删除所有的meta-article关系对，并且如果要
     * 删除的文章是某一个tag的最后一个文章时，就将该tag一起删除。
     *
     * @param delete            要删除的文章主键
     * @param metaExample（不含条件）
     * @param tExample          TRelationExample（不含条件）
     * @param id                要删除的tagId
     */
    private void deleteRelationAndUselessTags(Long delete, TMetaExample metaExample, TRelationExample tExample, Long id) {
        tExample.createCriteria()
                .andAIdEqualTo(delete)
                .andMIdEqualTo(id);
        isDeleteSuccess(relationMapper.deleteByExample(tExample));
        tExample.clear();

        tExample.createCriteria()
                .andMIdEqualTo(id);
        if (relationMapper.countByExample(tExample) == 0) {
            metaExample.createCriteria().andMIdEqualTo(id);
            metaMapper.deleteByExample(metaExample);
        }
        tExample.clear();
    }

    private void isDeleteSuccess(int i) {
        if (i != 1)
            throw new DeleteRecordException();
    }

    private boolean artIdExist(Long aId) {
        return articleMapper.isArticleIdExist(aId);
    }

    private Long fromContentGetMetaId(TMetaExample metaExample, String content, String type) {
        // content不能为null
        metaExample.createCriteria()
                .andMContentEqualTo(content)
                .andMTypeEqualTo(type);

        if (metaMapper.countByExample(metaExample) != 1)
            throw new TagException();
        metaExample.clear();
        return metaMapper.selectByExample(metaExample).get(0).getMId();
    }

    private List<String> splitTags(String tags) {
        return Arrays.asList(tags.split(";"));
    }

    /**
     * 这个函数的作用在于补全TArticle中的CategoryId属性，并且如果是新的Cate的话
     * 就将该Cate和对应的Relation存储进去.
     * 并且还要再TRelation表中存储对应的记录
     * 此处不允许有表中未存储的category_content
     *
     * @param article TArticle
     */
    private void saveCateRelation(TArticle article) {
        String cate = article.getCategory();
        if (!StringUtils.hasText(cate)) {
            article.setCategoryId(null);
            return;
        }
        TMetaExample example = new TMetaExample();
        example.createCriteria()
                .andMTypeEqualTo(MetaType.CATEGORY.getType())
                .andMContentEqualTo(cate);
        // 此处只有1和0两种值
        List<TMeta> cates = metaMapper.selectByExample(example);
        if (cates.size() == 1) {
            TRelationKey key = new TRelationKey(article.getAId(), cates.get(0).getMId());
            relationMapper.insert(key);
            articleMapper.updateCategoryId(cates.get(0).getMId(), article.getAId());
        } else throw new CategoryNotFoundException();
    }

    /**
     * 将tags里的tag提取出来并进行检查，如果数据库中有tag，则建立对应的Relation，如果没有
     * 则先存储该tag，再建立对应的Relation
     */
    private void saveTagAndRelations(TArticle article) {
        if (StringUtils.hasText(article.getTags())) {
            splitTags(article.getTags()).forEach(t -> {
                // 对每个tag，如果已有，则存储relation，如果没有，则新建并存储relation
                TMetaExample example = new TMetaExample();
                example.createCriteria()
                        .andMTypeEqualTo(MetaType.TAG.getType())
                        .andMContentEqualTo(t);
                List<TMeta> metas = metaMapper.selectByExample(example);
                if (metas.size() == 1) {
                    TRelationKey key = new TRelationKey(article.getAId(), metas.get(0).getMId());
                    relationMapper.insert(key);
                } else {
                    createAndSaveTagRelation(t, article);
                }
            });
        }
    }

    private void createAndSaveTagRelation(String t, TArticle article) {
        TMeta tag = new TMeta();
        tag.setMContent(t);
        tag.setMType(MetaType.TAG.getType());
        metaMapper.insert(tag);
        // todo 确认这里的tag是否可以得到mid
        assert tag.getMId() != null;
        TRelationKey key = new TRelationKey(article.getAId(), tag.getMId());
        relationMapper.insert(key);
    }

}
