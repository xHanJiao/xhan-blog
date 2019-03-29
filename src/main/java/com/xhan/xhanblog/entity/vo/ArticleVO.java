package com.xhan.xhanblog.entity.vo;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dao.TArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleVO extends MetaArtVO {

    private Long aId;

    private Date createTime;

    private Integer hits;

    private Integer commentNums;

    private String status;

    public ArticleVO(TArticle t) {
        aId = t.getAId();
        createTime = t.getCreateTime();
        hits = t.getHits();
        commentNums = t.getCommentNums();
        status = t.getStatus();
        setATitle(t.getATitle());
        setTags(t.getTags());
        setCategory(t.getCategory());
    }

    public boolean isBanished() {
        return getStatus().equals(Status.BANISHED.getStatus());
    }
}
