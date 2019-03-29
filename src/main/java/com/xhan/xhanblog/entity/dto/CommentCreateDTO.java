package com.xhan.xhanblog.entity.dto;

import com.xhan.xhanblog.entity.vo.CommentVO;
import com.xhan.xhanblog.util.PatternKit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommentCreateDTO extends CommentVO {

    @Email
    private String email;

    private String personalUrl;

    private String ip;

    private static final Integer COMMENT_MAX_LEN = 140;

    private static final Integer MAX_VISITOR_NAME_LEN = 20;

    public boolean islegal() {
        if (!PatternKit.isEmail(getEmail()))
            return false;
        if (getContent().length() > COMMENT_MAX_LEN)
            return false;
        if (getUsername().length() > MAX_VISITOR_NAME_LEN)
            return false;
        return true;
    }

}
