package com.deange.gimgur.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deange.gimgur.R;
import com.deange.gimgur.model.ImageResult;
import com.deange.gimgur.model.QueryResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ImageAdapter extends PrefetchAdapter {

    private Context mContext;
    private List<ImageResult> mResults = new ArrayList<>();
    private LinkedList<QueryResponse> mQueryResponses = new LinkedList<>();

    public ImageAdapter(final Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return (mResults == null) ? 0 : mResults.size();
    }

    @Override
    public ImageResult getItem(final int position) {
        return mResults.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    public void addResponse(final QueryResponse response) {
        if (!mQueryResponses.contains(response)) {
            // We always use 8 results per page
            mQueryResponses.add(response);
            response.setResultPage(mQueryResponses.size() * 8);
            mResults.addAll(response.getImages());
        }
    }

    public QueryResponse getLastResponse() {
        try {
            return mQueryResponses.getLast();
        } catch (final NoSuchElementException e) {
            // Weird API throw if there are no elements...why can't it just return null!?
            return null;
        }
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // Need to call this for the prefetching capabilities
        super.getView(position, convertView, parent);

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup rootView;

        if (convertView == null) {
            rootView = (ViewGroup) inflater.inflate(R.layout.list_item_image, null);

        } else {
            rootView = (ViewGroup) convertView;
        }

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.list_item_imageview);
        final ImageResult result = getItem(position);

        final float aspectRatio = (float) result.getHeight() / (float) result.getWidth();
        final int width = parent.getWidth();
        final int height = (int) (aspectRatio * width);

        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        imageView.setLayoutParams(params);

        Picasso.with(mContext)
                .load(result.getUrl())
                .centerInside().resize(width, height)
                .into(imageView);

        return rootView;
    }
}
