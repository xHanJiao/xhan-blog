package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.dao.TArticle;
import com.xhan.xhanblog.entity.vo.UploadArtVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.xhan.xhanblog.controller.ControllerConst.*;
import static com.xhan.xhanblog.service.ArticleHelper.DEFAULT_PAGE_SIZE_STRING;
import static org.springframework.http.HttpStatus.OK;

@Controller
public class ArticleController extends BaseController {

    @GetMapping(value = ARTICLE_URL + "/{aId}")
    public ResponseEntity<Map<String, Object>> getArticleById(@PathVariable Long aId,
                                                              HttpSession session) {
        if (aId == null || aId < 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        TArticle article = articleHelper.getArticleById(aId);
        boolean modifiable = session.getAttribute("user") != null;

        Map<String, Object> result = new HashMap<String, Object>() {{
            put("article", article);
            put("modifiable", modifiable);
        }};
        return new ResponseEntity<>(result, OK);
    }

    @GetMapping(value = LIST_ARTICLE_URL)
    public ResponseEntity<?> listArticle(@RequestParam(required = false, defaultValue = "0") Integer page,
                                         @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE_STRING) Integer pageSize,
                                         HttpSession session) {
        if (page < 0 || pageSize < 0)
            return ResponseEntity.badRequest().build();
        boolean isAdmin = session.getAttribute("user") != null;

        return ResponseEntity.status(HttpStatus.OK)
                .body(articleHelper.getArticleList(page, pageSize, isAdmin));
    }

    @PostMapping(value = UPLOAD_ARTICLE_URL)
    public ResponseEntity<?> uploadArticle(@RequestBody @Valid UploadArtVO artVO, BindingResult result,
                                           HttpSession session) throws IOException {
        checkIfLogin(session);
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError(), HttpStatus.BAD_REQUEST);
        }
        articleHelper.save(artVO);
        return getRedirectEntityToCenter();
    }

    // todo updateArticle

    @PostMapping(value = RECOVER_ARTICLE_URL + "/{recover}")
    public ResponseEntity<?> recoverArticle(@PathVariable Long recover,
                                            HttpSession session) {
        checkIfLogin(session);
        if (recover == null || recover < 0)
            return ResponseEntity.badRequest().body("error with article id");

        articleHelper.recoverById(recover);
        return getRedirectEntityToCenter();
    }

    @PostMapping(value = DELETE_ARTICLE_URL + "/{delete}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long delete,
                                           HttpSession session) {
        checkIfLogin(session);
        if (delete == null || delete < 0)
            return ResponseEntity.badRequest().body("error with article id");

        articleHelper.banishById(delete);
        return getRedirectEntityToCenter();
    }

    private ResponseEntity<?> getRedirectEntityToCenter() {
        return ResponseEntity
                .status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, CONTROL_CENTER_URL)
                .build();
    }
}
