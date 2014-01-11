package com.deange.gimgur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryResult {

    @SerializedName("results")
    private List<ImageResult> mImages;

    public List<ImageResult> getImages() {
        return mImages;
    }
}
