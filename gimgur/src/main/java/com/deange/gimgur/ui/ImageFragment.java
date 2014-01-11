package com.deange.gimgur.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.deange.gimgur.R;
import com.deange.gimgur.model.ImgurAlbum;
import com.deange.gimgur.model.QueryResponse;
import com.deange.gimgur.net.HttpTask;
import com.deange.gimgur.net.UrlConstants;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageFragment extends Fragment implements PrefetchAdapter.OnPrefetchListener {

    private static final String TAG = ImageFragment.class.getSimpleName();

    private ImageAdapter mAdapter;

    private final HttpTask.HttpCallback<QueryResponse> GET_CALLBACK = new HttpTask.HttpCallback<QueryResponse>() {
        @Override
        public void onHttpResponseReceived(final QueryResponse queryResult) {

            // Gotta make sure we are still attached to the activity!
            if ((getActivity() != null) && (queryResult != null)) {
                mAdapter.addResponse(queryResult);
                mAdapter.notifyDataSetChanged();

//                HttpTask.post(queryResult.getImages(), POST_CALLBACK);
            }
        }
    };

    private final HttpTask.HttpCallback<ImgurAlbum> POST_CALLBACK = new HttpTask.HttpCallback<ImgurAlbum>() {
        @Override
        public void onHttpResponseReceived(final ImgurAlbum queryResult) {

        }
    };

    public ImageFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        mAdapter = new ImageAdapter(getActivity());
        mAdapter.setOnPrefetchListener(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listview = (ListView) rootView.findViewById(R.id.fragment_main_grid_view);
        listview.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        doQuery("bears");
    }

    private void doQuery(final String queryString) {
        final URL url;

        if (!TextUtils.isEmpty(queryString)) {
            try {
                if (mAdapter.getLastResponse() == null) {
                    url = new URL(UrlConstants.getQueryUrl(queryString));

                } else {
                    url = new URL(mAdapter.getLastResponse().getNextUrl());
                }

                HttpTask.get(url, QueryResponse.class, GET_CALLBACK);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPrefetchRequested(final int position) {
        doQuery("bears");
    }

}
