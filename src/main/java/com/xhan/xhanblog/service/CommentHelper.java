package com.xhan.xhanblog.service;

import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dto.CommentCreateDTO;
import com.xhan.xhanblog.entity.vo.CommentVO;
import com.xhan.xhanblog.entity.vo.RecentCommentVO;

import javax.validation.Valid;
import java.util.List;

public interface CommentHelper {


    Integer DAY = 3;

    Integer R_PAGE_SIZE = 5;

    // 不是获得未浏览的评论，是获得最近的新评论
    List<RecentCommentVO> getRecentComments();

    /**
     * 只得到父评论，不会得到子评论，管理员可以看到被屏蔽的评论内容，
     * 普通用户只能看到被屏蔽
     */
    List<CommentVO> getCommentOfArticle(Long aId, Integer page, Integer pageSize, boolean isAdmin);

    // 当user不为null时为作者存的 创建的时候要注意将有后代设置为false，如有
    // 父评论，则将其有后代设置为true 还要修改对应文章的评论数
    void saveComment(@Valid CommentCreateDTO comment, TUser user);

    // 评论和文章的不同点：评论不是文章，没有文章就没有评论，但是没有父评论可以显示
    // 评论已删除
    void banishCommentById(Long delete);

    List<CommentVO> geDecentComments(Long coId, Boolean isAdmin, int page, int pageSize);
}
