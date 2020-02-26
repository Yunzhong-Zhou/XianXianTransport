package com.transport.xianxian.model;

import java.io.Serializable;

/**
 * Created by zyz on 2019-09-30.
 */
public class Fragment3Model implements Serializable {

    /**
     * nickname : 18306043086
     * head :
     */

    private String nickname;
    private String head;
    private int msg;
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }
}
