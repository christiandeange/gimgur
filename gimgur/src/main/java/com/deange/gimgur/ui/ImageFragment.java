package com.deange.gimgur.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.deange.gimgur.R;
import com.deange.gimgur.misc.GsonController;
import com.deange.gimgur.model.QueryResponse;
import com.deange.gimgur.net.Constants;
import com.deange.gimgur.net.OKHttpRequest;

import java.io.IOException;
import java.net.URL;

public class ImageFragment extends Fragment implements PrefetchAdapter.OnPrefetchListener {

    private static final String TAG = ImageFragment.class.getSimpleName();

    private ListView mListview;
    private ImageAdapter mAdapter;

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

        mListview = (ListView) rootView.findViewById(R.id.fragment_main_grid_view);
        mListview.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        doQuery("bears");
    }

    @Override
    public void onPrefetchRequested(final int position) {
        doQuery("bears");
    }

    private void doQuery(final String query) {
        new AsyncTask<Void, Void, QueryResponse>() {

            @Override
            protected QueryResponse doInBackground(final Void... params) {

                try {

                    final URL url;
                    if (mAdapter.getLastResponse() == null) {
                        url = new URL(Constants.getUrl(query));

                    } else {
                        url = new URL(mAdapter.getLastResponse().getNextUrl());
                    }

                    final String response = OKHttpRequest.get(url);
                    return GsonController.getInstance().from(response, QueryResponse.class);

                } catch (final IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(final QueryResponse queryResult) {

                Log.v(TAG, "queryResult = " + queryResult);

                // Gotta make sure we are still attached to the activity!
                if ((getActivity() != null) && (queryResult != null)) {
                    mAdapter.addResponse(queryResult);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }.execute();
    }
}
