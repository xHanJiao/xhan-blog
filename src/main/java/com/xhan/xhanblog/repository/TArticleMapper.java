package com.xhan.xhanblog.repository;

import com.xhan.xhanblog.entity.dao.TArticle;
import com.xhan.xhanblog.entity.dao.TArticleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TArticleMapper {
    long countByExample(TArticleExample example);

    int deleteByExample(TArticleExample example);

    int deleteByPrimaryKey(Long aId);

    int insert(TArticle record);

    int insertSelective(TArticle record);

    List<TArticle> selectByExampleWithBLOBs(TArticleExample example);

    List<TArticle> selectByExample(TArticleExample example);

    TArticle selectByPrimaryKey(Long aId);

    int updateByExampleSelective(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByExampleWithBLOBs(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByExample(@Param("record") TArticle record, @Param("example") TArticleExample example);

    int updateByPrimaryKeySelective(TArticle record);

    int updateByPrimaryKeyWithBLOBs(TArticle record);

    int updateByPrimaryKey(TArticle record);

    int updateCategoryContent(@Param("cate") String cate, @Param("cateId") Long cateId);

    default boolean isArticleIdExist(Long aId){
        if (aId == null)
            return false;
        TArticleExample example = new TArticleExample();
        example.createCriteria().andAIdEqualTo(aId);
        return countByExample(example) == 1;
    }

    int updateCategoryId(@Param("cateId") Long cateId, @Param("aId") Long aId);
}