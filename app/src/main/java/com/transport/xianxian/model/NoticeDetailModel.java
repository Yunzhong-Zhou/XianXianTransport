package com.transport.xianxian.model;

import java.io.Serializable;

/**
 * Created by zyz on 2019-10-03.
 */
public class NoticeDetailModel implements Serializable {
    /**
     * id : 5
     * type : 3
     * title : 测试极光推送
     * message : 测试极光推送测试极光推送
     * detail :
     * created_at : 2019-12-03 16:11
     */

    private int id;
    private int type;
    private String title;
    private String message;
    private String detail;
    private String created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
