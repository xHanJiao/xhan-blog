package com.xhan.xhanblog.repository;

import com.xhan.xhanblog.entity.dao.TComment;
import com.xhan.xhanblog.entity.dao.TCommentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TCommentMapper {
    long countByExample(TCommentExample example);

    int deleteByExample(TCommentExample example);

    int deleteByPrimaryKey(Long coId);

    int insert(TComment record);

    int insertSelective(TComment record);

    List<TComment> selectByExample(TCommentExample example);

    TComment selectByPrimaryKey(Long coId);

    int updateByExampleSelective(@Param("record") TComment record, @Param("example") TCommentExample example);

    int updateByExample(@Param("record") TComment record, @Param("example") TCommentExample example);

    int updateByPrimaryKeySelective(TComment record);

    int updateByPrimaryKey(TComment record);

    default boolean isCommentExistById(Long id){
        TCommentExample example = new TCommentExample();
        example.createCriteria().andCoIdEqualTo(id);

        return selectByExample(example).size() == 1;
    }
}