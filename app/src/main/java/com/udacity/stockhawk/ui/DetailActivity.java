package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.CursorPagerAdapter;
import com.udacity.stockhawk.data.Contract;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ViewPager mPager;
    private CursorPagerAdapter mPagerAdapter;
    private static final int QUOTE_ADAPTER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(QUOTE_ADAPTER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        switch (id) {
            case QUOTE_ADAPTER: {
                if (data.moveToFirst()) {
                    mPagerAdapter = new CursorPagerAdapter(getSupportFragmentManager(), this.getClass(), Contract.Quote.QUOTE_COLUMNS, data);
                    mPager.setAdapter(mPagerAdapter);
                }
                break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
