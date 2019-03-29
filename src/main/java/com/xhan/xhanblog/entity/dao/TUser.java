package com.xhan.xhanblog.entity.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TUser {
    private Long uId;

    private String userName;

    private String password;

    private String lastRegisterIp;

    private String email;

    private String homeUrl;

    private String groupName;

    public TUser(Long uId) {
        this.uId = uId;
    }
}

