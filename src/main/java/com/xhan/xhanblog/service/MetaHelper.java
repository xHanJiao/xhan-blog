package com.xhan.xhanblog.service;

import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.dto.UpdateCateDTO;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.MetaVO;

import javax.validation.Valid;
import java.util.List;

public interface MetaHelper {
    List<MetaVO> getAllCategories();

    List<MetaVO> getAllTags();

    /**
     * 在这个函数里，删除某个类目的记录，并且将该记录下所有的文章的类目属性设为null（默认）
     * @param delete 要删除的类目名称，在controller以校验过hasText
     */
    void deleteCate(String delete);

    /**
     * 要检查有没有重名，内容是否合法，描述是否合法，parent是否真实存在
     * @param create 类目名
     */
    void createCategory(CreateCateDTO create);

    /**
     * 对Category进行修改，可以修改content，description和parent
     */
    void updateCategory(@Valid UpdateCateDTO update);

    List<ArticleVO> getArticleByCateId(Long cate, Integer page, Integer pageSize, boolean isAdmin);

    // 先得到对应的tagId，再通过Relation表和Article的连接得到所有的文章
    List<ArticleVO> getArticleByTag(String tag, Integer page, Integer pageSize, boolean isAdmin);
}
