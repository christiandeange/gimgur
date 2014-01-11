package com.deange.gimgur.model;

import com.google.gson.annotations.SerializedName;

public class QueryResponse {

    @SerializedName("responseData")
    private QueryResult mQueryResult;

    @SerializedName("responseDetails")
    private String mResponseDetails;

    @SerializedName("responseStatus")
    private int mResponseCode;


    public int getResponseCode() {
        return mResponseCode;
    }

    public String getResponseDetails() {
        return mResponseDetails;
    }

    public QueryResult getQueryResult() {
        return mQueryResult;
    }
}
