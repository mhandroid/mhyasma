
package com.android.mh.yasma.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Geo implements Serializable {

    @SerializedName("lat")
    private String lat;
    @SerializedName("lng")
    private String lng;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

}
