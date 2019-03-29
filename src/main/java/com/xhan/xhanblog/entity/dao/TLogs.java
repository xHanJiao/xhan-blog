package com.xhan.xhanblog.entity.dao;

import lombok.Data;

@Data
public class TLogs {
    private Long lId;

    private String action;

    private String data;

    private Integer authorId;

    private String ip;

    private Integer created;

}