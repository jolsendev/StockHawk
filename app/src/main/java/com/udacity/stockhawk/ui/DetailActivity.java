package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.CursorPagerAdapter;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.fragments.StockDetailFragment;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ViewPager mPager;
    private CursorPagerAdapter mPagerAdapter;
    private static final int QUOTE_ADAPTER = 0;
    public static final String POSITION = "position";
    private int position;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundleExtras = getIntent().getExtras();
        mUri = getIntent().getData();
        position = bundleExtras.getInt(POSITION);
        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        mPager = (ViewPager)findViewById(R.id.pager);
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
        CursorLoader cursor = null;
        //(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        switch (id){
            case QUOTE_ADAPTER:{
                return new CursorLoader(this,
                        mUri,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);
            }
        }
        return cursor;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        switch (id) {
            case QUOTE_ADAPTER: {
                if (data.moveToFirst()) {
                    StockDetailFragment sDF = new StockDetailFragment();
                    mPagerAdapter = new CursorPagerAdapter(getSupportFragmentManager(), sDF.getClass(), Contract.Quote.QUOTE_COLUMNS, data);
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
