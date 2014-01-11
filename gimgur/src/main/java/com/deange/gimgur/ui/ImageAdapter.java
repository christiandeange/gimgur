package com.deange.gimgur.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.deange.gimgur.R;
import com.deange.gimgur.model.ImageResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageResult> mResults;

    public ImageAdapter(final Context context, final List<ImageResult> results) {
        mContext = context;
        mResults = results;
    }

    @Override
    public int getCount() {
        return (mResults == null) ? 0 : mResults.size();
    }

    @Override
    public ImageResult getItem(final int position) {
        return (mResults == null) ? null : mResults.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

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

        Picasso.with(mContext)
                .load(result.getUrl())
                .centerInside().resize(width, height)
                .into(imageView);

        return rootView;
    }
}
