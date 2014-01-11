package com.deange.gimgur.net;

import android.text.TextUtils;

public final class Constants {

    public static String getUrl(final String searchQuery) {

        final String htmlQuery = TextUtils.htmlEncode(searchQuery);

        return "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q=" + htmlQuery + "&rsz=8";
    }

    private Constants() {
        // Uninstantiable
    }

}
