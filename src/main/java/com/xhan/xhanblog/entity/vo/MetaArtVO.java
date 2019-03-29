package com.xhan.xhanblog.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MetaArtVO {

    @NotBlank(message = "标题不能为空")
    private String aTitle;

    private String category;

    private String tags;

}
