package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.CursorPagerAdapter;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.fragments.StockDetailFragment;
import com.udacity.stockhawk.pager_helpers.ZoomOutPageTransformer;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{

    private ViewPager mPager;
    private CursorPagerAdapter mPagerAdapter;
    private static final int QUOTE_ADAPTER = 0;
    public static final String POSITION = "mPosition";
    private int mPosition;
    private Uri mUri;
    private TextView tvOneMonthButton;
    private TextView tvThreeMonthButton;
    private TextView tvSixMonth;
    private TextView tvOneYear;
    private String sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Bundle bundleExtras = getIntent().getExtras();
        mUri = getIntent().getData();
        mPosition = bundleExtras.getInt(POSITION);
        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        mPager = (ViewPager)findViewById(R.id.pager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTextViews();
        sharedPref = PrefUtils.getDateRangePreference(this);
        setPrefRangeButton();
    }

    private void initTextViews() {
        tvOneMonthButton = (TextView) findViewById(R.id.tv_one_month);
        tvOneMonthButton.setOnClickListener(this);
        tvThreeMonthButton = (TextView)findViewById(R.id.tv_three_months);
        tvThreeMonthButton.setOnClickListener(this);
        tvSixMonth = (TextView)findViewById(R.id.tv_six_months);
        tvSixMonth.setOnClickListener(this);
        tvOneYear = (TextView)findViewById(R.id.tv_one_year);
        tvOneYear.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportLoaderManager().initLoader(QUOTE_ADAPTER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursor = null;

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
                    mPagerAdapter = new CursorPagerAdapter(getSupportFragmentManager(), sDF.getClass(), Contract.Quote.QUOTE_COLUMNS, data, mPosition);
                    mPager.setAdapter(mPagerAdapter);
                    //mPagerAdapter.swapCursor(data);
                    mPager.setPageTransformer(true, new ZoomOutPageTransformer());
                }
                break;
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
    }

    private void setPrefRangeButton() {
        if(sharedPref != null){
            switch (sharedPref){
                case StockDetailFragment.PREF_ONE_MONTH:{
                    tvOneMonthButton.setTextColor(Color.RED);
                    break;
                }
                case StockDetailFragment.PREF_THREE_MONTHS:{
                    tvThreeMonthButton.setTextColor(Color.RED);
                    break;
                }
                case StockDetailFragment.PREF_SIX_MONTHS:{
                    tvSixMonth.setTextColor(Color.RED);
                    break;
                }
                case StockDetailFragment.PREF_ONE_YEAR:{
                    tvOneYear.setTextColor(Color.RED);
                    break;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.tv_one_month:{
                mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("one_month").build();
                PrefUtils.setDateRangePreference(this, StockDetailFragment.PREF_ONE_MONTH);
                resetButtonColor();
                tvOneMonthButton.setTextColor(Color.RED);
                restartLoader();
                break;
            }
            case R.id.tv_three_months:{
                mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("three_months").build();
                PrefUtils.setDateRangePreference(this, StockDetailFragment.PREF_THREE_MONTHS);
                resetButtonColor();
                tvThreeMonthButton.setTextColor(Color.RED);
                restartLoader();
                break;
            }
            case R.id.tv_six_months:{
                mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("six_months").build();
                PrefUtils.setDateRangePreference(this, StockDetailFragment.PREF_SIX_MONTHS);
                resetButtonColor();
                tvSixMonth.setTextColor(Color.RED);
                restartLoader();
                break;
            }
            case R.id.tv_one_year:{
                mUri = Contract.Quote.URI;
                PrefUtils.setDateRangePreference(this, StockDetailFragment.PREF_ONE_YEAR);
                resetButtonColor();
                tvOneYear.setTextColor(Color.RED);
                restartLoader();
                break;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetButtonColor() {
        tvOneMonthButton.setTextColor(this.getColor(R.color.textColor));
        tvOneYear.setTextColor(this.getColor(R.color.textColor));
        tvSixMonth.setTextColor(this.getColor(R.color.textColor));
        tvThreeMonthButton.setTextColor(this.getColor(R.color.textColor));
    }

}
