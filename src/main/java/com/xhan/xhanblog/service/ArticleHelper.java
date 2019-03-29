package com.xhan.xhanblog.service;

import com.xhan.xhanblog.entity.dao.TArticle;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.UploadArtVO;

import javax.validation.Valid;
import java.util.List;

public interface ArticleHelper {

    Integer DEFAULT_PAGE_SIZE = 5;
    String DEFAULT_PAGE_SIZE_STRING = "5";

    // 不同权限的用户看到的列表是不一样的
    List<ArticleVO> getArticleList(boolean isAdmin);

    List<ArticleVO> getArticleList(int page, int pageSize, boolean isAdmin);

    /**
     * 因为将tag的生命周期和文章结合起来了，所以在上传文章时，需要注意
     * 对tag的处理，如果是新tag，也要存一个新的
     * @param artVO UploadArtVO
     */
    void save(@Valid UploadArtVO artVO);

    /**
     * 在执行这个方法的时候，要注意更新点击数
     */
    TArticle getArticleById(Long aId);

    /**
     * 删除文章时要注意对tag的处理，文章DO中的tags属性是用
     * aaa;bbb;ccc这样的形式拼接起来的，所以要将它们逐一取出，
     * 得到对应的metaId，并从Relation中将纪录删除
     * @param delete 要删除的文章主键
     */
    void deleteById(Long delete);

    /**
     * 当第一次调用这个方法时，会将文章的状态转为BANISHED，第二次调用则会删除文章
     */
    void banishById(Long delete);

    void recoverById(Long recover);

}
