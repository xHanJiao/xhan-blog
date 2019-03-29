package com.xhan.xhanblog.entity.dao;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.vo.UploadArtVO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class TArticle {
    private Long aId;

    private String aTitle;

    private Date createTime;

    private String category;

    private Long categoryId;

    private String tags;

    private Integer hits;

    private Integer commentNums;

    private Byte allowComment;

    private String status;

    private String aContent;


    public TArticle(@Valid UploadArtVO artVO) {
        setCategory(artVO.getCategory());
        setAContent(artVO.getAContent());
        setTags(artVO.getTags());
        setATitle(artVO.getATitle());
        setHits(0);
        setAllowComment((byte) 1);
        setCommentNums(0);
        setStatus(Status.PUBLISHED.getStatus());
        setCreateTime(new Date());
    }

    public TArticle(Long aId, int hit) {
        setAId(aId);
        setHits(hit);
    }

    public TArticle(Long aId, String status) {
        this.aId = aId;
        this.status = status;
    }

    public TArticle(@NotNull Long aId) {
        setAId(aId);
    }

    public boolean isBanished() {
        return getStatus().equals(Status.BANISHED.getStatus());
    }
}