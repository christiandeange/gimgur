package com.deange.gimgur.ui;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PrefetchAdapter extends BaseAdapter {

    private static final String TAG = PrefetchAdapter.class.getSimpleName();

    private int mPrefetchLimit = 2;
    private OnPrefetchListener mOnPrefetchListener = Fallback.INSTANCE;

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        if (position >= getCount() - mPrefetchLimit) {
            mOnPrefetchListener.onPrefetchRequested(position);
        }

        return null;
    }

    // Fallback for avoiding NPEs
    public void setOnPrefetchListener(final OnPrefetchListener listener) {
        Log.v(TAG, "setOnPrefetchListener()");

        mOnPrefetchListener = (listener == null) ? Fallback.INSTANCE : listener;
    }

    public int getPrefetchLimit() {
        return mPrefetchLimit;
    }

    public void setPrefetchLimit(final int prefetchLimit) {
        mPrefetchLimit = prefetchLimit;
    }

    // Public prefetch interface
    public interface OnPrefetchListener {
        public void onPrefetchRequested(final int position);
    }

    private static final class Fallback implements OnPrefetchListener {
        public static final Fallback INSTANCE = new Fallback();

        @Override
        public void onPrefetchRequested(final int position) {
            Log.w(TAG, "Fallback: onPrefetchRequested()");
        }
    }
}
