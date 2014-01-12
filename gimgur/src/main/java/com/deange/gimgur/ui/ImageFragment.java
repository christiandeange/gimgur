package com.deange.gimgur.ui;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.deange.gimgur.R;
import com.deange.gimgur.misc.Utils;
import com.deange.gimgur.model.ImageResult;
import com.deange.gimgur.model.ImgurAlbum;
import com.deange.gimgur.model.QueryResponse;
import com.deange.gimgur.net.HttpTask;
import com.deange.gimgur.net.UrlConstants;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment implements PrefetchAdapter.OnPrefetchListener, ImageAdapter.OnItemChangeListener, View.OnClickListener {

    public static final String TAG = ImageFragment.class.getSimpleName();

    private ImageAdapter mAdapter;
    private TextView mItemsText;
    private ImageButton mUploadButton;

    private RefreshAffordanceProvider mRefreshAffordanceProvider = Fallback.INSTANCE;

    private final HttpTask.HttpCallback<QueryResponse> GET_CALLBACK = new HttpTask.HttpCallback<QueryResponse>() {
        @Override
        public void onHttpResponseReceived(final QueryResponse queryResult) {

            // Gotta make sure we are still attached to the activity!
            if ((getActivity() != null) && (queryResult != null)) {
                mAdapter.addResponse(queryResult);
                mAdapter.notifyDataSetChanged();

                mRefreshAffordanceProvider.onRefreshAffordanceRequested(false);
            }
        }
    };

    private final HttpTask.HttpCallback<ImgurAlbum> POST_CALLBACK = new HttpTask.HttpCallback<ImgurAlbum>() {
        @Override
        public void onHttpResponseReceived(final ImgurAlbum album) {
            if ((getActivity() != null) && (album != null)) {

                final String albumLocation = UrlConstants.getImgurAlbum(album.getId());

                new AlertDialog.Builder(getActivity())
                        .setMessage(albumLocation)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                            }
                        })
                        .setNegativeButton(android.R.string.copy, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE))
                                        .setPrimaryClip(ClipData.newPlainText("Copied Text", albumLocation));
                                Toast.makeText(getActivity(), R.string.dialog_copied_clipboard, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        }
    };

    public ImageFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

        mAdapter = new ImageAdapter(getActivity(), this);
        mAdapter.setOnPrefetchListener(this);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final ListView listview = (ListView) rootView.findViewById(R.id.fragment_main_grid_view);
        listview.setAdapter(mAdapter);

        final ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.fragment_main_upload_root);
        Utils.setLayoutTransition(viewGroup);

        mUploadButton = (ImageButton) rootView.findViewById(R.id.fragment_main_upload);
        mUploadButton.setOnClickListener(this);

        mItemsText = (TextView) rootView.findViewById(R.id.fragment_main_items);
        updateText(0);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    public void doQuery(final String queryString) {
        final URL url;

        if (!TextUtils.isEmpty(queryString)) {
            try {

                if (!TextUtils.equals(queryString, mAdapter.getCurrentQuery())) {
                    mAdapter.clear();
                }

                if (mAdapter.getLastResponse() == null) {
                    url = new URL(UrlConstants.getQueryUrl(queryString));

                } else {
                    url = new URL(mAdapter.getLastResponse().getNextUrl());
                }

                mAdapter.setCurrentQuery(queryString);
                mRefreshAffordanceProvider.onRefreshAffordanceRequested(true);
                HttpTask.get(url, QueryResponse.class, GET_CALLBACK);

            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateText(final int itemsSaved) {
        final boolean isZero = (itemsSaved == 0);

        mItemsText.setText(isZero ? "" : String.valueOf(itemsSaved));
        mItemsText.setVisibility(isZero ? View.GONE : View.VISIBLE);
        mUploadButton.setColorFilter(isZero ? Color.LTGRAY : Color.TRANSPARENT);
        mUploadButton.setEnabled(!isZero);
    }

    private void handleUpload() {

        final List<String> urls = new ArrayList<>();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final ImageResult imageResult = mAdapter.getItem(i);

            if (imageResult.isSelected()) {
                urls.add(imageResult.getUrl());
            }
        }

        mRefreshAffordanceProvider.onRefreshAffordanceRequested(true);
        HttpTask.post(urls, POST_CALLBACK);
    }

    public void setRefreshAffordanceProvider(final RefreshAffordanceProvider refreshAffordanceProvider) {
        Log.v(TAG, "setRefreshAffordanceProvider()");

        mRefreshAffordanceProvider = refreshAffordanceProvider == null
                ? Fallback.INSTANCE : refreshAffordanceProvider;
    }

    @Override
    public void onPrefetchRequested(final int position) {
        doQuery(mAdapter.getCurrentQuery());
    }

    @Override
    public void onItemStateChanged(final int position) {

        int itemsSaved = 0;

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final ImageResult imageResult = mAdapter.getItem(i);

            if (imageResult.isSelected()) {
                itemsSaved++;
            }
        }

        updateText(itemsSaved);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.fragment_main_upload:
                handleUpload();
                break;
        }
    }

    public interface RefreshAffordanceProvider {
        public void onRefreshAffordanceRequested(final boolean show);
    }

    private static final class Fallback implements RefreshAffordanceProvider {
        public static final Fallback INSTANCE = new Fallback();

        @Override
        public void onRefreshAffordanceRequested(final boolean show) {
            Log.w(TAG, "Fallback: onRefreshAffordanceRequested()");
        }
    }
}
