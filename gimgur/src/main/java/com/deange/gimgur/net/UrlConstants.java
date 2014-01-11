package com.deange.gimgur.net;

import android.text.TextUtils;

public final class UrlConstants {

    public static String getQueryUrl(final String searchQuery) {
        final String htmlQuery = TextUtils.htmlEncode(searchQuery);
        return "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q=" + htmlQuery + "&rsz=8&start=0";
    }

    public static String getPostUrl() {
        return "https://aqueous-harbor-2434.herokuapp.com/";
    }

    private UrlConstants() {
        // Uninstantiable
    }

}
