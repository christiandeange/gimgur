package com.deange.gimgur.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.deange.gimgur.R;
import com.deange.gimgur.model.ImageResult;
import com.deange.gimgur.model.QueryResponse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class ImageAdapter extends PrefetchAdapter {

    private Context mContext;
    private OnItemChangeListener mListener;
    private List<ImageResult> mResults = new ArrayList<>();
    private LinkedList<QueryResponse> mQueryResponses = new LinkedList<>();

    private String mCurrentQuery;

    public ImageAdapter(final Context context, final OnItemChangeListener listener) {
        mContext = context;
        mListener = listener;
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

    public String getCurrentQuery() {
        return mCurrentQuery;
    }

    public void setCurrentQuery(final String currentQuery) {
        mCurrentQuery = currentQuery;
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

    public void setListener(final OnItemChangeListener listener) {
        mListener = listener;
    }

    public void clear() {
        mResults.clear();
        mQueryResponses.clear();
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

        final ImageResult result = getItem(position);

        final TextView description = (TextView) rootView.findViewById(R.id.list_item_description);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.list_item_imageview);
        final ProgressBar progress = (ProgressBar) rootView.findViewById(R.id.list_item_progress);
        final CompoundButton compoundButton = (CompoundButton) rootView.findViewById(R.id.list_item_compound);

        final float aspectRatio = (float) result.getHeight() / (float) result.getWidth();
        final int width = parent.getWidth();
        final int height = (int) (aspectRatio * width);

        final ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        imageView.setLayoutParams(params);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                compoundButton.performClick();
            }
        });

        compoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                result.setSelected(isChecked);
                if (mListener != null) {
                    mListener.onItemStateChanged(position);
                }
            }
        });

        compoundButton.setChecked(result.isSelected());
        description.setText(result.getDescription());

        final Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                done();
            }

            @Override
            public void onError() {
                done();
            }

            private void done() {
                progress.setVisibility(View.GONE);
                description.setVisibility(View.GONE);
            }
        };

        Picasso.with(mContext)
                .load(result.getUrl())
                .centerInside().resize(width, height)
                .into(imageView, callback);

        return rootView;
    }

    public interface OnItemChangeListener {
        public void onItemStateChanged(final int position);
    }

}
