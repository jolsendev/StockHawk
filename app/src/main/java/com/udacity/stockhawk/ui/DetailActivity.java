package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockPagerAdapter;
import com.udacity.stockhawk.fragments.StockDetailFragment;

public class DetailActivity extends AppCompatActivity {

    private ViewPager mPager;
    private StockPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new StockPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        if(savedInstanceState == null){
//            StockDetailFragment details = new StockDetailFragment();
//            getSupportFragmentManager().beginTransaction().add(R.id.detail_container, details, "fragTag").commit();
//        }
    }
}
