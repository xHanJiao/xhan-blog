package com.xhan.xhanblog.entity.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TRelationKey {
    private Long mId;

    private Long aId;

    public TRelationKey(Long aId, Long mId) {
        this.mId = mId;
        this.aId = aId;
    }

}