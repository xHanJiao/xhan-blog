package com.xhan.xhanblog.entity.vo;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dao.TComment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentVO extends RecentCommentVO {

    private Long parent;

    private String username;

    private boolean hasDecent;

    private String status;

    public boolean isBanished(){
        return getStatus().equals(Status.BANISHED.getStatus());
    }

    public CommentVO(TComment comment){
        setStatus(comment.getStatus());
        setParent(comment.getParent());
        setUsername(comment.getUsername());
        setAId(comment.getaId());
        setHasDecent(comment.getHasDecent());
        setCoId(comment.getCoId());
        setContent(comment.getContent());
        setCreateTime(comment.getCreateTime());
    }

}
