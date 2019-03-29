package com.xhan.xhanblog.entity.dto;

import lombok.Data;

import javax.validation.constraints.Min;

import static org.springframework.util.StringUtils.hasText;

@Data
public class CreateCateDTO {

    private String content;

    private String description;

    public boolean isCreateTextLegal() {
        if (!hasText(content) || content.length() > 20)
            return false;

        if (hasText(description) && description.length() > 255)
            return false;

        return true;
    }

}
