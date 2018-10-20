
package com.android.mh.yasma.model;

import com.google.gson.annotations.SerializedName;

public class Album {

    @SerializedName("userId")
    private Integer userId;
    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
