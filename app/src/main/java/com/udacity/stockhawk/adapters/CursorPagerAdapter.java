package com.udacity.stockhawk.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.common.collect.ImmutableList;
import com.udacity.stockhawk.data.Contract;

public class CursorPagerAdapter<F extends Fragment> extends FragmentStatePagerAdapter {
    private final Class<F> fragmentClass;
    private final ImmutableList<String> projection;
    private Cursor cursor;
    private int mPosition;

    public CursorPagerAdapter(FragmentManager fm, Class<F> fragmentClass, ImmutableList<String> projection, Cursor cursor, int position) {
        super(fm);
        this.fragmentClass = fragmentClass;
        this.projection = projection;
        this.cursor = cursor;
        this.mPosition = position;
    }

    @Override
    public F getItem(int position) {
//        position = mPosition;
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

    @Override
    public int getCount() {
        if (cursor == null)
            return 0;
        else
            return cursor.getCount();
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
}