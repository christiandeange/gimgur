package com.deange.gimgur.model;

import com.google.gson.annotations.SerializedName;

public class ImageResult {

    @SerializedName("unescapedUrl")
    private String mUrl;

    @SerializedName("contentNoFormatting")
    private String mDescription;

    @SerializedName("width")
    private int mWidth;

    @SerializedName("height")
    private int mHeight;

    // Used in UI for toggling upload to album or not
    private boolean mIsSelected;

    public String getUrl() {
        return mUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(final boolean isSelected) {
        mIsSelected = isSelected;
    }
}
