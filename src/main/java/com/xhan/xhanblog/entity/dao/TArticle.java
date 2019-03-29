package com.xhan.xhanblog.entity.dao;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.vo.UploadArtVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
        setaContent(artVO.getAContent());
        setTags(artVO.getTags());
        setaTitle(artVO.getATitle());
        setHits(0);
        setAllowComment((byte) 1);
        setCommentNums(0);
        setStatus(Status.PUBLISHED.getStatus());
        setCreateTime(new Date());
    }

    public TArticle(Long aId, int hit) {
        setaId(aId);
        setHits(hit);
    }

    public TArticle(Long aId, String status) {
        this.aId = aId;
        this.status = status;
    }

    public TArticle(@NotNull Long aId) {
        setaId(aId);
    }
    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public String getaTitle() {
        return aTitle;
    }

    public void setaTitle(String aTitle) {
        this.aTitle = aTitle == null ? null : aTitle.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags == null ? null : tags.trim();
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public Integer getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(Integer commentNums) {
        this.commentNums = commentNums;
    }

    public Byte getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(Byte allowComment) {
        this.allowComment = allowComment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getaContent() {
        return aContent;
    }

    public void setaContent(String aContent) {
        this.aContent = aContent == null ? null : aContent.trim();
    }
}