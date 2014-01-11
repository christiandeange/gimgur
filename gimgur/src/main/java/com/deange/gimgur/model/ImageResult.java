package com.deange.gimgur.model;

import com.google.gson.annotations.SerializedName;

public class ImageResult {

    @SerializedName("unescapedUrl")
    private String mUrl;

    @SerializedName("contentNoFormatting")
    private String mDescription;

    public String getUrl() {
        return mUrl;
    }

    public String getDescription() {
        return mDescription;
    }
}
