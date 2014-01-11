package com.deange.gimgur.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.deange.gimgur.model.ImageResult;

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
    public Object getItem(final int position) {
        return (mResults == null) ? null : mResults.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        final LayoutInflater inflater = LayoutInflater.from(mContext);

        return null;
    }
}
