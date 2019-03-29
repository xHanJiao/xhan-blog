package com.xhan.xhanblog.entity.dao;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dto.CommentCreateDTO;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class TComment {
    private Long coId;

    private String content;

    private String mail;

    private String url;

    private String ip;

    private String username;

    private Long userId;

    private String status;

    private Long parent;

    private Long aId;

    private Date createTime;

    private Boolean hasDecent;

    public TComment(CommentCreateDTO comment) {
        setHasDecent(false);
        setaId(comment.getAId());
        setContent(comment.getContent());
        setCreateTime(comment.getCreateTime());
        setIp(comment.getIp());
        setStatus(Status.PUBLISHED.getStatus());
        setUrl(comment.getPersonalUrl());
        setMail(comment.getEmail());
        setParent(comment.getParent());
    }

    public TComment(Long coId) {
        this.setCoId(coId);
    }

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getaId() {
        return aId;
    }

    public void setaId(Long aId) {
        this.aId = aId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getHasDecent() {
        return hasDecent;
    }

    public void setHasDecent(Boolean hasDecent) {
        this.hasDecent = hasDecent;
    }

    public void banish() {
        setStatus(Status.BANISHED.getStatus());
    }
}