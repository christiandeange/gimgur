package com.deange.gimgur.net;

import android.os.AsyncTask;
import android.util.Log;

import com.deange.gimgur.misc.GsonController;
import com.deange.gimgur.model.ImageResult;
import com.deange.gimgur.model.ImgurAlbum;
import com.deange.gimgur.model.QueryResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class HttpTask {

    private static final String TAG = HttpTask.class.getSimpleName();

    public static <T> void get(final URL url, final Class<T> clazz, final HttpCallback<T> callback) {
        new AsyncTask<Void, Void, T>() {

            @Override
            protected T doInBackground(final Void... params) {

                try {
                    final String response = OKHttpRequest.get(url);
                    return GsonController.getInstance().from(response, clazz);

                } catch (final IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(final T result) {
                Log.v(TAG, "result = " + result);

                if (callback != null) {
                    callback.onHttpResponseReceived(result);
                }
            }
        }.execute();
    }

    public static void post(final List<ImageResult> images, final HttpCallback<ImgurAlbum> callback) {
        new AsyncTask<Void, Void, ImgurAlbum>() {

            @Override
            protected ImgurAlbum doInBackground(final Void... params) {

                try {

                    final URL url = new URL(UrlConstants.getPostUrl());

                    final List<String> urls = new ArrayList<>();
                    for (final ImageResult result : images) {
                        urls.add(result.getUrl());
                    }

                    final ImgurAlbum album = new ImgurAlbum();
                    album.setImages(urls);

                    // Post to the gimgur server!
                    // Unfortunately this usually takes some time...O(n) for the amount of
                    // images you wish to save
                    final String jsonBody = GsonController.getInstance().to(album);
                    final String response = OKHttpRequest.post(url, jsonBody);
                    return GsonController.getInstance().from(response, ImgurAlbum.class);

                } catch (final IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(final ImgurAlbum album) {
                Log.v(TAG, "album = " + album);

                if (callback != null) {
                    callback.onHttpResponseReceived(album);
                }
            }
        }.execute();
    }

    private HttpTask() {
        // Uninstantiable
    }

    public interface HttpCallback<T> {
        public void onHttpResponseReceived(final T object);
    }
}
