package com.xhan.xhanblog.entity.vo;

import com.xhan.xhanblog.entity.dao.TMeta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetaVO {
    // 要有id啊
    private Long mId;

    private String mType;

    private String mContent;

    private Long parent;

    public MetaVO(TMeta tMeta) {
        mId = tMeta.getMId();
        mType = tMeta.getMType();
        mContent = tMeta.getMContent();
        parent = tMeta.getParent();
    }
}
