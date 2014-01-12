package com.deange.gimgur.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.deange.gimgur.R;

public class MainActivity extends FragmentActivity {

    private MenuItem mRefreshMenu;

    private ImageFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mFragment == null) {
            mFragment = (ImageFragment) getSupportFragmentManager().findFragmentByTag(ImageFragment.TAG);

            if (mFragment == null) {
                mFragment = new ImageFragment();
            }
        }

        if (!mFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(
                    R.id.container, mFragment, ImageFragment.TAG).commit();
        }

        // yolo
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void setRefreshProgressState(final boolean show) {
        if (mRefreshMenu != null) {
            if (show) {
                mRefreshMenu.setActionView(R.layout.menu_progress_layout);
                mRefreshMenu.setVisible(true);

            } else {
                mRefreshMenu.setActionView(null);
                mRefreshMenu.setVisible(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        mRefreshMenu = menu.findItem(R.id.action_progress);
        setRefreshProgressState(false);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (mFragment != null) {
                    mFragment.doQuery(query);
                }
                return (mFragment != null);
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
