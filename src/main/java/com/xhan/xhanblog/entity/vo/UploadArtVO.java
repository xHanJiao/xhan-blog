package com.xhan.xhanblog.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class UploadArtVO extends MetaArtVO{

    public static final int MAX_TITLE_LEN = 140;
    public static final int MAX_CONTENT_LEN = 200000;

    @NotBlank(message = "内容不能为空")
    private String aContent;

    public boolean isTextOverflow() {
        return getATitle().length() <= MAX_TITLE_LEN && getAContent().length() <= MAX_CONTENT_LEN;
    }
}
