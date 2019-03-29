package com.xhan.xhanblog.entity.vo;

import com.xhan.xhanblog.entity.dao.TComment;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class RecentCommentVO {

    private Date createTime;

    private Long coId;

    @NotNull
    private Long aId;

    @NotBlank
    private String content;

    private Long parent;

    public RecentCommentVO(TComment c){
        aId = c.getAId();
        createTime = c.getCreateTime();
        coId = c.getCoId();
        content = c.getContent();
        parent = c.getParent();
    }
}
