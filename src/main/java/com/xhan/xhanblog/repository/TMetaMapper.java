package com.xhan.xhanblog.repository;

import com.xhan.xhanblog.entity.dao.TMeta;
import com.xhan.xhanblog.entity.dao.TMetaExample;
import java.util.List;

import com.xhan.xhanblog.util.MetaType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TMetaMapper {
    long countByExample(TMetaExample example);

    int deleteByExample(TMetaExample example);

    int deleteByPrimaryKey(Long mId);

    int insert(TMeta record);

    int insertSelective(TMeta record);

    List<TMeta> selectByExample(TMetaExample example);

    TMeta selectByPrimaryKey(Long mId);

    int updateByExampleSelective(@Param("record") TMeta record, @Param("example") TMetaExample example);

    int updateByExample(@Param("record") TMeta record, @Param("example") TMetaExample example);

    int updateByPrimaryKeySelective(TMeta record);

    int updateByPrimaryKey(TMeta record);

    default boolean isMetaExistByContent(String content){
        TMetaExample example = new TMetaExample();
        example.createCriteria()
                .andMTypeEqualTo(MetaType.CATEGORY.getType())
                .andMContentEqualTo(content);
        return selectByExample(example).size() == 1;
    }

    Long getCateIdByContent(String delete);
}