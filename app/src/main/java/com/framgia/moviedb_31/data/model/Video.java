package com.framgia.moviedb_31.data.model;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("key")
    private String mKey;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }
}
