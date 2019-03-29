package com.xhan.xhanblog.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static org.springframework.util.StringUtils.hasText;

@Data
@EqualsAndHashCode(callSuper = false, of = "cId")
public class UpdateCateDTO extends CreateCateDTO {
    @Min(0)
    @NotNull
    private Long cId;

    /**
     * 检查内容是否合法，content，parent和description不能都为null
     * 不为null 的值必须符合长度要求
     * @return
     */
    public boolean isUpdateTextLegal() {

        if (hasText(getDescription()) && getDescription().length() > 255)
            return false;

        if (hasText(getContent()) && getContent().length() > 20)
            return false;

        if (getContent() == null && getParent() == null && getDescription() == null)
            return false;

        return true;
    }
}
