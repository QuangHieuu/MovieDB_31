package com.framgia.moviedb_31.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BaseCredit {
    @SerializedName("cast")
    private List<Actor> mActorList;
    @SerializedName("crew")
    private List<Production> mProductionList;
    @SerializedName("results")
    private List<Video> mVideoList;

    public List<Actor> getActorList() {
        return mActorList;
    }

    public List<Production> getProductionList() {
        return mProductionList;
    }

    public List<Video> getVideoList() {
        return mVideoList;
    }

    public void setVideoList(List<Video> videoList) {
        mVideoList = videoList;
    }
}
