package com.deange.gimgur.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.deange.gimgur.R;

public class ImageFragment extends Fragment {

    private static final String TAG = ImageFragment.class.getSimpleName();

    private StaggeredGridView mGridView;

    public ImageFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (StaggeredGridView) rootView.findViewById(R.id.fragment_main_grid_view);
        mGridView.setColumnCount(2);

        final int max = 100;
        final String[] items = new String[max];
        for (int i = 0; i < max; i++) {
            items[i] = "";
            int times = (int) (Math.random() * 10);
            for (int j = 0; j < times; j++) {
                items[i] += "AAAAAAA";
            }

            items[i] += "AAAAAAA " + i;
        }


        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.test_list_item, items);

        mGridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        Log.v(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);


    }
}
