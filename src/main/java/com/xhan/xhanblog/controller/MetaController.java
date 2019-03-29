package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.dto.UpdateCateDTO;
import com.xhan.xhanblog.entity.vo.ArticleVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

import static com.xhan.xhanblog.controller.ControllerConst.CATEGORY_URL;
import static org.springframework.util.StringUtils.hasText;

@RestController
public class MetaController extends BaseController {

    @PostMapping(value = CATEGORY_URL, params = "delete")
    public ResponseEntity<?> deleteCategory(@RequestParam String delete, HttpSession session){
        // todo 能不能把这个要check的抽取到切面中
        checkIfLogin(session);
        if(!hasText(delete))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);

        metaHelper.deleteCate(delete);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = CATEGORY_URL, params = "create")
    public ResponseEntity<?> createCategory(@Valid CreateCateDTO create, HttpSession session){
        checkIfLogin(session);

        metaHelper.createCategory(create);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping(value = CATEGORY_URL, params = "update")
    public ResponseEntity<?> updateCategory(@Valid UpdateCateDTO update, HttpSession session){
        checkIfLogin(session);

        metaHelper.updateCategory(update);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = CATEGORY_URL, params = "cate")
    public ResponseEntity<List<ArticleVO>> getArticleByCate(@RequestParam String cate,
                                                            @RequestParam(required = false, defaultValue = "0") Integer page,
                                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        // 这个不用校验了
        if (!hasText(cate))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<ArticleVO> arts = metaHelper.getArticleByCate(cate, page, pageSize);
        return new ResponseEntity<>(arts, HttpStatus.OK);
    }



}
