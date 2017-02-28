package com.udacity.stockhawk.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockDetailAdapter;
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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StockDetailAdapter mDetailAdapter;
    private Uri mQuoteUri;
    private View view;
    private RecyclerView chartRecyclerView;
    private String stock;
    private Uri mQuoteUri2;
    //private int mPosition;
    private Uri mUri;
    private int position;

    public StockDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment StockDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockDetailFragment newInstance(String param1) {
        StockDetailFragment fragment = new StockDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        //getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
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
        chartRecyclerView = (RecyclerView) view.findViewById(R.id.chart_recycler_view);
        chartRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        chartRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                System.out.println("BB "+chartRecyclerView);

            }
        });
        mDetailAdapter = new StockDetailAdapter(getContext(), null, 0, this);

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
                chartRecyclerView.setAdapter(mDetailAdapter);
                //TabLayout tabLayout = (TabLayout) view.findViewById(R.id.sliding_tabs);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                linearLayoutManager.scrollToPosition(getPosition());
                chartRecyclerView.setLayoutManager(linearLayoutManager);
                mDetailAdapter.setCursor(cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailAdapter.setCursor(null);
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
    }

}
