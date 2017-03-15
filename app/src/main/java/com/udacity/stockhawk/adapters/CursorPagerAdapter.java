package com.udacity.stockhawk.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.google.common.collect.ImmutableList;
import com.udacity.stockhawk.data.Contract;

import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;

public class CursorPagerAdapter<F extends Fragment> extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
    private final Class<F> fragmentClass;
    private final ImmutableList<String> projection;
    private Cursor cursor;
    private int mRealPosition;
    private int mAdapterPosition = 0;
    private boolean realPositionBeenUsed = false;
    private int firstPosition;

    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass, ImmutableList<String> projection, Cursor cursor, int position) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.cursor = cursor;
        this.mRealPosition = position;
    }

    @Override
    public F getItem(int position) {
        if (cursor == null) // shouldn't happen
            return null;

        cursor.moveToPosition(position);
        F frag;
        try {
            frag = fragmentClass.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        Bundle args = new Bundle();

        args.putString(projection.get(Contract.Quote.POSITION_SYMBOL),
                cursor.getString(Contract.Quote.POSITION_SYMBOL));

        args.putDouble(projection.get(Contract.Quote.POSITION_PRICE),
                cursor.getDouble(Contract.Quote.POSITION_PRICE));

        args.putDouble(projection.get(Contract.Quote.POSITION_ABSOLUTE_CHANGE),
            cursor.getDouble(Contract.Quote.POSITION_ABSOLUTE_CHANGE));

        args.putDouble(projection.get(Contract.Quote.POSITION_PERCENTAGE_CHANGE),
                cursor.getDouble(Contract.Quote.POSITION_PERCENTAGE_CHANGE));

        args.putString(projection.get(Contract.Quote.POSITION_HISTORY),
                cursor.getString(Contract.Quote.POSITION_HISTORY));
        frag.setArguments(args);
        return frag;
    }

    private int getRealPosition(int position) {

        //if reslPosition = getPosition()
            //do nothing
            //set realPosition
        //if position is oneLess
//        if (!realPositionBeenUsed){
//            firstPosition = getPosition();
//            realPostion = getPosition();
//            realPositionBeenUsed = true;
//        }

        return position;
    }


    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else{
             int count = cursor.getCount();
            return count;
        }
    }


    public void swapCursor(Cursor c) {
        if (cursor == c)
            return;

        this.cursor = c;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setPosition(int position) {
        this.mRealPosition = position;

    }
    private int getPosition(){
        return this.mRealPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(positionOffset == SCROLL_STATE_IDLE){
            System.out.println("!!!!!!!!!!!!!!!!!!positionOffSet position: "+position);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}