package com.udacity.stockhawk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockDetailAdapter;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.fragments.StockDetailFragment;

public class DetailActivity extends AppCompatActivity {

    private DetailActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);



        if(savedInstanceState == null){
//            Toolbar toolbar = (Toolbar)findViewById(R.id.detail_toolbar);
//            setSupportActionBar(toolbar);
           // getSupportActionBar().setDisplayShowTitleEnabled(false);
            StockDetailFragment details = new StockDetailFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_layout, details, "fragTag").commit();
        }



    }

}
