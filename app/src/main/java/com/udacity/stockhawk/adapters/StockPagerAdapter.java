package com.udacity.stockhawk.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.udacity.stockhawk.fragments.StockDetailFragment;

/**
 * Created by A5W5NZZ on 3/1/2017.
 */

public class StockPagerAdapter extends FragmentStatePagerAdapter {
    public StockPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new StockDetailFragment().newInstance(position);
    }

    @Override
    public int getCount() {
        return 0;
    }
}
