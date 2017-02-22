package com.udacity.stockhawk.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockDetailAdapter;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link StockDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class StockDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int QUOTE_ADAPTER = 0;
    private static final String ARG_PARAM1 = "param1";
    public static final String POSITION = "position";
    public static final String PREF_ONE_MONTH = "one_month";
    public static final String PREF_SIX_MONTHS = "six_months";
    public static final String PREF_ONE_YEAR = "one_year";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Callback mListener;

    private StockDetailAdapter mDetailAdapter;
    private Uri mQuoteUri;
    private View view;
    private RecyclerView chartRecyclerView;
    private String stock;
    private Uri mQuoteUri2;
    private int mPosition;
    private Button oneMonth, sixMonth, oneYear;
    private Uri mUri;


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

        getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getActivity().getIntent().getData();
        stock = Contract.Quote.getStockFromUri(mUri);
        mUri = Contract.Quote.URI;
        Bundle extra  = getActivity().getIntent().getExtras();
        mPosition = extra.getInt(POSITION);

        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(QUOTE_ADAPTER, null, this);
    }

    /**
     *
     * 6/2/2017,64.00\n30/1/2017,63.68\n23/1/2017,65.779999\n17/1/2017,62.740002\n9/1/2017,62.700001\n3/1/2017,62.84\n27/12/2016,62.139999\n19/12/2016,63.240002\n12/12/2016,62.299999\n5/12/2016,61.970001\n28/11/2016,59.25\n21/11/2016,60.529999\n14/11/2016,60.349998\n7/11/2016,59.02\n31/10/2016,58.709999\n24/10/2016,59.869999\n17/10/2016,59.66\n10/10/2016,57.419998\n3/10/2016,57.799999\n26/9/2016,57.599998\n19/9/2016,57.43\n12/9/2016,57.25\n6/9/2016,56.209999\n29/8/2016,57.669998\n22/8/2016,58.029999\n15/8/2016,57.619999\n8/8/2016,57.939999\n1/8/2016,57.959999\n25/7/2016,56.68\n18/7/2016,56.57\n11/7/2016,53.700001\n5/7/2016,52.299999\n27/6/2016,51.16\n20/6/2016,49.830002\n13/6/2016,50.130001\n6/6/2016,51.48\n31/5/2016,51.790001\n23/5/2016,52.32\n16/5/2016,50.619999\n9/5/2016,51.080002\n2/5/2016,50.389999\n25/4/2016,49.869999\n18/4/2016,51.779999\n11/4/2016,55.650002\n4/4/2016,54.419998\n28/3/2016,55.57\n21/3/2016,54.209999\n14/3/2016,53.490002\n7/3/2016,53.07\n29/2/2016,52.029999\n22/2/2016,51.299999\n16/2/2016,51.82\n8/2/2016,50.50\n1/2/2016,50.16\n25/1/2016,55.09\n19/1/2016,52.290001\n11/1/2016,50.990002\n4/1/2016,52.330002\n28/12/2015,55.48\n21/12/2015,55.669998\n14/12/2015,54.130001\n7/12/2015,54.060001\n30/11/2015,55.91\n23/11/2015,53.93\n16/11/2015,54.189999\n9/11/2015,52.84\n2/11/2015,54.919998\n26/10/2015,52.639999\n19/10/2015,52.869999\n12/10/2015,47.509998\n5/10/2015,47.110001\n28/9/2015,45.57\n21/9/2015,43.939999\n14/9/2015,43.48\n8/9/2015,43.48\n31/8/2015,42.610001\n24/8/2015,43.93\n17/8/2015,43.07\n10/8/2015,47.00\n3/8/2015,46.740002\n27/7/2015,46.700001\n20/7/2015,45.939999\n13/7/2015,46.619999\n6/7/2015,44.610001\n29/6/2015,44.400002\n22/6/2015,45.259998\n15/6/2015,46.099998\n8/6/2015,45.970001\n1/6/2015,46.139999\n26/5/2015,46.860001\n18/5/2015,46.900002\n11/5/2015,48.299999\n4/5/2015,47.75\n27/4/2015,48.66\n20/4/2015,47.869999\n13/4/2015,41.619999\n6/4/2015,41.720001\n30/3/2015,40.290001\n23/3/2015,40.970001\n16/3/2015,42.880001\n9/3/2015,41.380001\n2/3/2015,42.360001\n23/2/2015,43.849998\n17/2/2015,43.860001\n13/2/2015,43.869999\n
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(false);
        view = inflater.inflate(R.layout.activity_detail, container, false);
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
        mDetailAdapter = new StockDetailAdapter(getContext(), null, 0);
        oneMonth = (Button) view.findViewById(R.id.btn_one_month);
        sixMonth = (Button) view.findViewById(R.id.btn_six_months);
        oneYear = (Button) view.findViewById(R.id.btn_one_year);
        oneMonth.setOnClickListener(this);
        sixMonth.setOnClickListener(this);
        oneYear.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                linearLayoutManager.scrollToPosition(mPosition);
                chartRecyclerView.setLayoutManager(linearLayoutManager);
                mDetailAdapter.setCursor(cursor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDetailAdapter.setCursor(null);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.btn_one_month:{
                //Toast.makeText(getContext(), "One month button", Toast.LENGTH_SHORT).show();
                mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("one_month").build();
                PrefUtils.setDateRangePreference(getActivity(), PREF_ONE_MONTH);
                restartLoader();
                break;
            }
            case R.id.btn_six_months:{
                //Toast.makeText(getContext(), "Six month button", Toast.LENGTH_SHORT).show();
                mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("six_months").build();
                PrefUtils.setDateRangePreference(getActivity(), PREF_SIX_MONTHS);
                restartLoader();
                break;
            }
            case R.id.btn_one_year:{
                //Toast.makeText(getContext(), "One year button", Toast.LENGTH_SHORT).show();
                mUri = Contract.Quote.URI;
                PrefUtils.setDateRangePreference(getActivity(), PREF_ONE_YEAR);
                restartLoader();
                break;
            }
        }
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Callback {
        // TODO: Update argument type and name
        void onStockDetailFragmentInteraction(Uri uri);
    }
}
