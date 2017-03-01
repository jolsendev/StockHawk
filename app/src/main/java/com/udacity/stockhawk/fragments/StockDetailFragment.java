package com.udacity.stockhawk.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockPagerAdapter;
import com.udacity.stockhawk.data.Contract;

public class StockDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // TODO: Rename parameter arguments, choose names that match

    private static final int QUOTE_ADAPTER = 0;

    private static final String ARG_PARAM1 = "param1";
    public static final String POSITION = "position";
    public static final String PREF_ONE_MONTH = "one_month";
    public static final String PREF_SIX_MONTHS = "six_months";
    public static final String PREF_ONE_YEAR = "one_year";
    public static final String PREF_THREE_MONTHS = "three_months";

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private View view;
    private Uri mUri;
    private int position;
    private String stock;

    public StockDetailFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position position of cursor
     * @return A new instance of fragment StockDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockDetailFragment newInstance(int position) {
        StockDetailFragment fragment = new StockDetailFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getActivity().getIntent().getData();
        stock = Contract.Quote.getStockFromUri(mUri);
        mUri = Contract.Quote.URI;
        Bundle extra  = getActivity().getIntent().getExtras();
        setPosition(extra.getInt(POSITION));

        //setActionToolBar

        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(QUOTE_ADAPTER, null, this);
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public int getPosition() {
        return position;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(false);
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new StockPagerAdapter(getActivity().getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursor = null;
        //(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
        switch (id){
            case QUOTE_ADAPTER:{
                return new CursorLoader(getContext(),
                        mUri,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);
            }
        }
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id){
            case QUOTE_ADAPTER:{

            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void restartLoader() {
        getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
    }
}
