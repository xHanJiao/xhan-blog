package com.xhan.xhanblog.entity.dto;

import com.xhan.xhanblog.util.PatternKit;
import lombok.Data;

import java.util.regex.Pattern;

import static org.springframework.util.StringUtils.hasText;


@Data
public class UpdateUserDTO {

    private String username;

    private String password;

    private String email;

    public boolean isLegal() {
        if (hasText(getPassword()) && passwordIllegal(getPassword()))
            return false;
        if (hasText(getUsername()) && usernameIllegal(getUsername()))
            return false;
        if (hasText(getEmail()) && emailIllegal(getEmail()))
            return false;
        return true;
    }

    private boolean usernameIllegal(String username) {
        return username.length() > 20;
    }

    private boolean passwordIllegal(String password) {
        String passwordRegex = "^([A-Z]|[a-z]|[0-9]|[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）――+|{}【】‘；：”“'。，、？]){6,20}$";
        return password.length() < 9 ||
                password.length() > 40 ||
                !Pattern.matches(passwordRegex, password);
    }

    private boolean emailIllegal(String email){
        return !PatternKit.isEmail(email);
    }

}
