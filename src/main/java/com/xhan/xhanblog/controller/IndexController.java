package com.xhan.xhanblog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import static com.xhan.xhanblog.controller.ControllerConst.SLASH;
import static org.springframework.http.HttpStatus.OK;

@Controller
public class IndexController extends BaseController {

    @GetMapping(value = SLASH)
    public ResponseEntity<?> index(HttpSession session) {
        return new ResponseEntity<>(getFaces(session), OK);
    }

}
