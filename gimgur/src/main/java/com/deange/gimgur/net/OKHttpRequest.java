package com.deange.gimgur.net;

import com.deange.gimgur.misc.Utils;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OKHttpRequest {

    public static String get(final URL url) throws IOException {

        final OkHttpClient client = new OkHttpClient();
        final HttpURLConnection connection = client.open(url);

        InputStream in = null;
        try {
            // Read the response.
            in = connection.getInputStream();
            final byte[] response = Utils.read(in);
            return new String(response, "UTF-8");
        } finally {
            Utils.closeQuietly(in);
        }
    }
}
