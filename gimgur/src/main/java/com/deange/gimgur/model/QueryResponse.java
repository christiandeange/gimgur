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

    @SerializedName("moreResultsUrl")
    private String mNextUrl;

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

    public String getNextUrl() {
        return mNextUrl;
    }

    public void setNextUrl(final String nextUrl) {
        mNextUrl = nextUrl
                .replace("http://www.google.com/images", "http://ajax.googleapis.com/ajax/services/search/images") + "&v=2.0&&rsz=8";
    }

    public void setResultPage(final int page) {
        if (mNextUrl != null) {
            mNextUrl = mNextUrl.replaceFirst("start=\\d*", "start=" + page);
        }
    }
}
