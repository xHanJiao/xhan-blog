package com.xhan.xhanblog.entity.dao;

import com.xhan.xhanblog.entity.dto.CreateCateDTO;
import com.xhan.xhanblog.entity.vo.MetaVO;
import com.xhan.xhanblog.util.MetaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TMeta extends MetaVO{
    /**
     * tag的创建和存储
     */
//    private Long mId;

//    private String mType;
//
//    private String mContent;

    private String description;

    public TMeta(CreateCateDTO create) {
        setDescription(create.getDescription());
        setParent(create.getParent());
        setMContent(create.getContent());
        setMType(MetaType.CATEGORY.getType());
    }

//    private Long parent;// 有没有必要要这个parent

}