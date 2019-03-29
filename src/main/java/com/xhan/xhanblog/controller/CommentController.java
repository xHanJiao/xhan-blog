package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.dao.TUser;
import com.xhan.xhanblog.entity.dto.CommentCreateDTO;
import com.xhan.xhanblog.entity.vo.CommentVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xhan.xhanblog.controller.ControllerConst.COMMENT_URL;
import static com.xhan.xhanblog.controller.ControllerConst.COMMENT_URL_DECENT;

@Controller
public class CommentController extends BaseController {
    // 要区分用户的评论和作者的评论

    @GetMapping(value = COMMENT_URL, params = "aId")
    public ResponseEntity<Map<String, Object>> getCommentOfArticle(@RequestParam Long aId, HttpSession session,
                                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (aId == null || aId < 0 || page < 0 || pageSize < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Boolean isAdmin = session.getAttribute("user") != null;
        Map<String, Object> result = new HashMap<String, Object>(){{
            put("comments", commentHelper.getCommentOfArticle(aId, page, pageSize, isAdmin));
            put("modifiable", isAdmin);
        }};
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = COMMENT_URL, params = "create")
    public ResponseEntity<?> createComment(@Valid CommentCreateDTO create,
                                           BindingResult result,
                                           HttpServletRequest request,
                                           HttpSession session){
        if (result.hasErrors())
            return ResponseEntity.badRequest().body(result.getFieldError());
        if (!create.islegal())
            return ResponseEntity.badRequest().body("illegal fields");

        TUser user = (TUser) session.getAttribute("user");
        saveIp(create, request);
        commentHelper.saveComment(create, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = COMMENT_URL, params = "banish")
    public ResponseEntity<?> banishComment(@RequestParam Long banish, HttpSession session){
        checkIfLogin(session); // 只有admin才能删除
        if(banish == null || banish < 0)
            return ResponseEntity.badRequest().build();
        commentHelper.banishCommentById(banish);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = COMMENT_URL_DECENT + "/{coId}")
    public ResponseEntity<?> getDecentComments(@PathVariable Long coId, HttpSession session,
                                               @RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        if (coId == null || coId < 0)
            return ResponseEntity.badRequest().build();

        Boolean isAdmin = session.getAttribute("user") != null;
        return ResponseEntity.ok(commentHelper.geDecentComments(coId, isAdmin, page, pageSize));
    }

    private void saveIp(@Valid CommentCreateDTO comment, HttpServletRequest request) {
        comment.setIp(request.getRemoteAddr());
    }

}
