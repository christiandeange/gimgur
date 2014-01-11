package com.deange.gimgur.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.deange.gimgur.R;
import com.deange.gimgur.misc.GsonController;
import com.deange.gimgur.model.QueryResponse;
import com.deange.gimgur.net.Constants;
import com.deange.gimgur.net.OKHttpRequest;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

public class ImageFragment extends Fragment {

    private static final String TAG = ImageFragment.class.getSimpleName();

    private ListView mListview;

    public ImageFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListview = (ListView) rootView.findViewById(R.id.fragment_main_grid_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        new AsyncTask<Void, Void, QueryResponse>() {

            @Override
            protected QueryResponse doInBackground(final Void... params) {

                try {
                    final URL url = new URL(Constants.getUrl("bears"));
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

                // Gotta make sure we are still attached
                if ((getActivity() != null) && (queryResult != null)) {
                    final ImageAdapter adapter = new ImageAdapter(getActivity(), queryResult.getImages());
                    mListview.setAdapter(adapter);
                }

            }
        }.execute();
    }
}
