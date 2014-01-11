package com.deange.gimgur.net;

import com.deange.gimgur.misc.Utils;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OKHttpRequest {

    private static final OkHttpClient sClient = new OkHttpClient();

    public static String get(final URL url) throws IOException {


        final HttpURLConnection connection = sClient.open(url);

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

    public static String post(final URL url, final String body) throws IOException {
        return post(url, body == null ? new byte[]{} : body.getBytes());
    }

    public static String post(final URL url, final byte[] body) throws IOException {
        final HttpURLConnection connection = sClient.open(url);
        OutputStream out = null;
        InputStream in = null;
        try {
            // Write the request
            connection.setRequestMethod("POST");
            out = connection.getOutputStream();
            out.write(body);
            out.close();

            // Read the response
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: "
                        + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            in = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            return reader.readLine();

        } finally {
            // Clean up
            Utils.closeQuietly(out);
            Utils.closeQuietly(in);
        }
    }


}
