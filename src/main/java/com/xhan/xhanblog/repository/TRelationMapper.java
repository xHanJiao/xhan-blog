package com.xhan.xhanblog.repository;

import com.xhan.xhanblog.entity.dao.TRelationExample;
import com.xhan.xhanblog.entity.dao.TRelationKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRelationMapper {
    long countByExample(TRelationExample example);

    int deleteByExample(TRelationExample example);

    int deleteByPrimaryKey(TRelationKey key);

    int insert(TRelationKey record);

    int insertSelective(TRelationKey record);

    List<TRelationKey> selectByExample(TRelationExample example);

    int updateByExampleSelective(@Param("record") TRelationKey record, @Param("example") TRelationExample example);

    int updateByExample(@Param("record") TRelationKey record, @Param("example") TRelationExample example);
}