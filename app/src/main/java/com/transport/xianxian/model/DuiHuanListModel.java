package com.transport.xianxian.model;

import java.io.Serializable;

/**
 * Created by zyz on 2019-10-08.
 */
public class DuiHuanListModel implements Serializable {
    /**
     * title : 话费
     * image : null
     * sub_title : ces
     * score : 100
     * crated_at : null
     */

    private String title;
    private String image;
    private String sub_title;
    private String score;
    private String created_at;
    private String status_text;

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
