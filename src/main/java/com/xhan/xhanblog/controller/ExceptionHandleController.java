package com.xhan.xhanblog.controller;

import com.xhan.xhanblog.exception.NotLoginException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.xhan.xhanblog.controller.ControllerConst.ADMIN_LOGIN_URL;

@ControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException e){
        // todo 细化异常处理
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(value = NotLoginException.class)
    public ResponseEntity<?> redirectNotLogin(){
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, ADMIN_LOGIN_URL)
                .build();
    }
}
