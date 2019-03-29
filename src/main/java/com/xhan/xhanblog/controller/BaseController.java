package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.Status;
import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import com.xhan.xhanblog.entity.vo.MetaVO;
import com.xhan.xhanblog.entity.vo.RecentCommentVO;
import com.xhan.xhanblog.exception.NotLoginException;
import com.xhan.xhanblog.service.ArticleHelper;
import com.xhan.xhanblog.service.CommentHelper;
import com.xhan.xhanblog.service.MetaHelper;
import com.xhan.xhanblog.service.UserHelper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@NoArgsConstructor
public abstract class BaseController {

    @Autowired protected UserHelper userHelper;
    @Autowired protected ArticleHelper articleHelper;
    @Autowired protected MetaHelper metaHelper;
    @Autowired protected CommentHelper commentHelper;

    void checkIfLogin(HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if (user == null)
            throw new NotLoginException("not login");
    }

    Map<String, Object> getFaces(HttpSession session) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("modifiable", session.getAttribute("user") != null);
        List<ArticleVO> articles = articleHelper.getArticleList(session.getAttribute("user") == null);
        // 如果非管理员则不能看到伪删除的文章

        map.put("articles", articles);
        return getTagComCate(map);
    }

    private Map<String, Object> getTagComCate(Map<String, Object> map) {
        List<RecentCommentVO> recentComments = commentHelper.getRecentComments();
        List<MetaVO> categories = metaHelper.getAllCategories();
        List<MetaVO> tags = metaHelper.getAllTags();
        map.put("tags", tags);
        map.put("categories", categories);
        map.put("rComment", recentComments);
        return map;
    }

}
