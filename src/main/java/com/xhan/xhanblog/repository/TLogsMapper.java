package com.xhan.xhanblog.repository;

import com.xhan.xhanblog.entity.dao.TLogs;
import com.xhan.xhanblog.entity.dao.TLogsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TLogsMapper {
    long countByExample(TLogsExample example);

    int deleteByExample(TLogsExample example);

    int deleteByPrimaryKey(Long lId);

    int insert(TLogs record);

    int insertSelective(TLogs record);

    List<TLogs> selectByExample(TLogsExample example);

    TLogs selectByPrimaryKey(Long lId);

    int updateByExampleSelective(@Param("record") TLogs record, @Param("example") TLogsExample example);

    int updateByExample(@Param("record") TLogs record, @Param("example") TLogsExample example);

    int updateByPrimaryKeySelective(TLogs record);

    int updateByPrimaryKey(TLogs record);
}