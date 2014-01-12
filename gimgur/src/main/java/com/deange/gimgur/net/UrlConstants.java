package com.deange.gimgur.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class UrlConstants {

    public static String getQueryUrl(final String searchQuery) {
        final String htmlQuery;
        try {
            htmlQuery = URLEncoder.encode(searchQuery, "UTF-8");
            return "http://ajax.googleapis.com/ajax/services/search/images?v=2.0&q=" + htmlQuery + "&rsz=8&start=0";
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getPostUrl() {
        return "https://aqueous-harbor-2434.herokuapp.com/";
    }

    public static String getImgurAlbum(final String id) {
        return "http://imgur.com/a/" + id;
    }

    private UrlConstants() {
        // Uninstantiable
    }

}
