package com.deange.gimgur.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QueryResponse {

    public QueryResponse(final String responseDetails, final int responseCode, final List<ImageResult> imageResults) {
        mResponseDetails = responseDetails;
        mResponseCode = responseCode;
        mImages = imageResults;
    }

    @SerializedName("responseDetails")
    private String mResponseDetails;

    @SerializedName("responseStatus")
    private int mResponseCode;

    @SerializedName("results")
    private List<ImageResult> mImages;

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getResponseDetails() {
        return mResponseDetails;
    }

    public List<ImageResult> getImages() {
        return mImages;
    }

    public void setImages(final List<ImageResult> images) {
        mImages = images;
    }
}
