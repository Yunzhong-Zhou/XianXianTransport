package com.transport.xianxian.model;

import java.io.Serializable;

/**
 * Created by zyz on 2018/6/26.
 */
public class LoginModel implements Serializable {
    /**
     * id : 3
     * mobile : 15823629471
     * role_type : 1
     * identity : 2
     * fresh_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjMsImlzcyI6Imh0dHA6XC9cL3d1bGl1LnpoZW55b25na2ouY29tXC9hcGlcL2RyaXZlclwvbG9naW4iLCJpYXQiOjE1NzE0NjcyOTcsImV4cCI6MTYwMzAwMzI5NywibmJmIjoxNTcxNDY3Mjk3LCJqdGkiOiI2ZmM2OGU1ZGZiOTliNmM1ZTc3MmY1N2UxOTg1NjdmNSJ9.48nbuWENJB07INJR8Ns_mbkV0PaeobCELfngGG9DXV8
     */

    private String id;
    private String mobile;
    private int is_certification;
    private int identity;
    private String fresh_token;
    private String hx_username;

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public int getIs_certification() {
        return is_certification;
    }

    public void setIs_certification(int is_certification) {
        this.is_certification = is_certification;
    }

    public String getFresh_token() {
        return fresh_token;
    }

    public void setFresh_token(String fresh_token) {
        this.fresh_token = fresh_token;
    }
}
