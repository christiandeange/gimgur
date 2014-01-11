package com.deange.gimgur.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImgurAlbum {

    @Expose(serialize = false)
    @SerializedName("id")
    private String mId;

    @Expose(deserialize = false)
    @SerializedName("imgs")
    private List<String> mImages;

    public String getId() {
        return mId;
    }

    public List<String> getImages() {
        return mImages;
    }

    public void setImages(final List<String> images) {
        mImages = images;
    }
}
