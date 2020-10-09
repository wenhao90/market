package com.market.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String method;

    private String mob;

    private String pwd;

    private String token;

    public LoginRequest(String method, String mob, String pwd) {
        this.method = method;
        this.mob = mob;
        this.pwd = pwd;
    }
}
