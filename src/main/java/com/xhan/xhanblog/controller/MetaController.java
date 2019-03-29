package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.dto.UpdateCateDTO;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.xhan.xhanblog.controller.ControllerConst.*;
import static org.springframework.util.StringUtils.hasText;

@Controller
public class MetaController extends BaseController {

    @PostMapping(value = DELETE_CATEGORY_URL + "/{content}", params = "delete")
    public ResponseEntity<?> deleteCategory(@PathVariable String content, HttpSession session) {
        checkIfLogin(session);
        if (!hasText(content))
            return ResponseEntity.badRequest().body("empty content");

        metaHelper.deleteCate(content);
        return ResponseEntity.ok("");
    }

    @PostMapping(value = CATEGORY_URL, params = "create")
    public ResponseEntity<?> createCategory(@Valid CreateCateDTO create, HttpSession session) {
        checkIfLogin(session);

        metaHelper.createCategory(create);
        return ResponseEntity.ok("");
    }


    @PostMapping(value = CATEGORY_URL, params = "update")
    public ResponseEntity<?> updateCategory(@Valid UpdateCateDTO update, HttpSession session) {
        checkIfLogin(session);

        metaHelper.updateCategory(update);
        return ResponseEntity.ok("");
    }

    @GetMapping(value = CATEGORY_ARTICLE_URL + "/{cId}")
    public ResponseEntity<List<ArticleVO>> getArticleByCate(@PathVariable Long cId, HttpSession session,
                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (cId == null || cId < 0 || page < 0 || pageSize < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isAdmin = session.getAttribute("user") != null;
        List<ArticleVO> arts = metaHelper.getArticleByCateId(cId, page, pageSize, isAdmin);
        return new ResponseEntity<>(arts, HttpStatus.OK);
    }

    @GetMapping(value = TAG_URL + "/{tag}")
    public ResponseEntity<List<ArticleVO>> getArticleByTag(@PathVariable String tag, HttpSession session,
                                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        if (!hasText(tag) || page < 0 || pageSize < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        boolean isAdmin = session.getAttribute("user") != null;
        List<ArticleVO> arts = metaHelper.getArticleByTag(tag, page, pageSize, isAdmin);
        return new ResponseEntity<>(arts, HttpStatus.OK);
    }

}
