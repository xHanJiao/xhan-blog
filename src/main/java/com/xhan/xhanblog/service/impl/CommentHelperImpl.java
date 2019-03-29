package com.xhan.xhanblog.service.impl;

import com.github.pagehelper.PageHelper;
import com.xhan.xhanblog.entity.dao.TArticle;
import com.xhan.xhanblog.entity.dao.TComment;
import com.xhan.xhanblog.entity.dao.TCommentExample;
import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dto.CommentCreateDTO;
import com.xhan.xhanblog.entity.vo.CommentVO;
import com.xhan.xhanblog.entity.vo.RecentCommentVO;
import com.xhan.xhanblog.exception.UpdateFailException;
import com.xhan.xhanblog.exception.article.ArticleNotFoundException;
import com.xhan.xhanblog.exception.comment.CommentNotFoundException;
import com.xhan.xhanblog.exception.comment.DeleteCommentException;
import com.xhan.xhanblog.exception.comment.SaveCommentException;
import com.xhan.xhanblog.repository.TArticleMapper;
import com.xhan.xhanblog.repository.TCommentMapper;
import com.xhan.xhanblog.service.CommentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CommentHelperImpl implements CommentHelper {

    private final TCommentMapper commentMapper;
    private final TArticleMapper articleMapper;

    @Autowired
    public CommentHelperImpl(TCommentMapper commentMapper, TArticleMapper articleMapper) {
        this.commentMapper = commentMapper;
        this.articleMapper = articleMapper;
    }

    @Override
    public List<RecentCommentVO> getRecentComments() {
        Date date = Date.from(LocalDateTime.now()
                .minusDays(CommentHelper.DAY)
                .atZone(ZoneId.systemDefault()).toInstant());
        TCommentExample example = new TCommentExample();
        example.createCriteria().andCreateTimeGreaterThanOrEqualTo(date);

        PageHelper.startPage(0, CommentHelper.R_PAGE_SIZE);
        List<TComment> comments = commentMapper.selectByExample(example);

        return comments.stream()
                .map(RecentCommentVO::new)
                .collect(toList());
    }

    @Override
    public List<CommentVO> getCommentOfArticle(Long aId, Integer page,
                                               Integer pageSize, boolean isAdmin) {
        if (!articleMapper.isArticleIdExist(aId))
            throw new ArticleNotFoundException();

        TCommentExample commentExample = new TCommentExample();
        commentExample.createCriteria()
                .andAIdEqualTo(aId)
                .andParentIsNull();

        return veilComment(isAdmin, commentExample, page, pageSize);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveComment(@Valid CommentCreateDTO comment, TUser user) {
        checkArticleAndChangeCommentNum(comment.getAId());
        setParentHasDec(comment);
        saveComm(comment, user);
    }

    @Override
    public void banishCommentById(Long delete) {
        if (!commentMapper.isCommentExistById(delete))
            throw new CommentNotFoundException();

        TComment comment = new TComment(delete);
        comment.banish();
        if (commentMapper.updateByPrimaryKeySelective(comment) != 1)
            throw new DeleteCommentException();
    }

    @Override
    public List<CommentVO> geDecentComments(Long coId, Boolean isAdmin, int page, int pageSize) {
        TCommentExample example = new TCommentExample();
        example.createCriteria().andParentEqualTo(coId);

        return veilComment(isAdmin, example, page, pageSize);
    }

    private List<CommentVO> veilComment(Boolean isAdmin, TCommentExample example, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CommentVO> vos = commentMapper.selectByExample(example)
                .stream().map(CommentVO::new).collect(toList());
        vos.forEach(cv -> {
            if (cv.isBanished() && !isAdmin) cv.setContent("");
        });
        return vos;
    }

    private void saveComm(@Valid CommentCreateDTO comment, TUser user) {
        TComment tComment = new TComment(comment);
        if (user != null) {
            tComment.setUserId(user.getUId());
            tComment.setUsername(user.getUserName());
        }
        if (commentMapper.insert(tComment) != 1)
            throw new SaveCommentException();
    }

    private void checkArticleAndChangeCommentNum(@NotNull Long aId) {
        if (!articleMapper.isArticleIdExist(aId))
            throw new ArticleNotFoundException();

        TArticle article = articleMapper.selectByPrimaryKey(aId);
        TArticle na = new TArticle(aId);
        na.setCommentNums(article.getCommentNums() + 1);
        articleMapper.updateByPrimaryKeySelective(na);
    }

    private void setParentHasDec(@Valid CommentCreateDTO comment) {
        if (comment.getParent() != null) {
            if (!commentMapper.isCommentExistById(comment.getParent()))
                throw new CommentNotFoundException("parent " + comment.getParent());
            TComment parent = new TComment();
            parent.setCoId(comment.getParent());
            parent.setHasDecent(true);
            if (commentMapper.updateByPrimaryKeySelective(parent) != 1)
                throw new UpdateFailException();
        }
    }

}
