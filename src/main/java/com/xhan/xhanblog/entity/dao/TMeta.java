package com.xhan.xhanblog.entity.dao;

import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.util.MetaType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TMeta {
    private Long mId;

    private String mType;

    private String mContent;

    private String description;

    public TMeta(CreateCateDTO create) {
        setDescription(create.getDescription());
        setMContent(create.getContent());
        setMType(MetaType.CATEGORY.getType());
    }
    public boolean isTag(){
        return getMType().equals(MetaType.TAG.getType());
    }

    public boolean isCate(){
        return getMType().equals(MetaType.CATEGORY.getType());
    }
}