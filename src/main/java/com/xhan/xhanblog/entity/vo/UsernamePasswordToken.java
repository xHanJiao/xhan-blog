package com.xhan.xhanblog.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsernamePasswordToken {

    @NotBlank(message = "用户名不能为空") String username;

    @NotBlank(message = "密码不能为空") String password;
}
