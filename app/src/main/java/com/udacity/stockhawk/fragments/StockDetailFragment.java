package com.udacity.stockhawk.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.adapters.StockDetailAdapter;
import com.udacity.stockhawk.chart_helpers.StockXAxesFormater;
import com.udacity.stockhawk.data.Contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link StockDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int QUOTE_ADAPTER = 0;
    private static final String ARG_PARAM1 = "param1";
    LineChart lineChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Callback mListener;

    private StockDetailAdapter mAdapter;
    private Uri mQuoteUri;
    private View view;
    private HorizontalScrollView mChartHorizontalScrollView;


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
        mAdapter = new StockDetailAdapter(getContext(), null, 0);
        getLoaderManager().restartLoader(QUOTE_ADAPTER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuoteUri = getActivity().getIntent().getData();

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    /**
     *
     * 6/2/2017,64.00\n30/1/2017,63.68\n23/1/2017,65.779999\n17/1/2017,62.740002\n9/1/2017,62.700001\n3/1/2017,62.84\n27/12/2016,62.139999\n19/12/2016,63.240002\n12/12/2016,62.299999\n5/12/2016,61.970001\n28/11/2016,59.25\n21/11/2016,60.529999\n14/11/2016,60.349998\n7/11/2016,59.02\n31/10/2016,58.709999\n24/10/2016,59.869999\n17/10/2016,59.66\n10/10/2016,57.419998\n3/10/2016,57.799999\n26/9/2016,57.599998\n19/9/2016,57.43\n12/9/2016,57.25\n6/9/2016,56.209999\n29/8/2016,57.669998\n22/8/2016,58.029999\n15/8/2016,57.619999\n8/8/2016,57.939999\n1/8/2016,57.959999\n25/7/2016,56.68\n18/7/2016,56.57\n11/7/2016,53.700001\n5/7/2016,52.299999\n27/6/2016,51.16\n20/6/2016,49.830002\n13/6/2016,50.130001\n6/6/2016,51.48\n31/5/2016,51.790001\n23/5/2016,52.32\n16/5/2016,50.619999\n9/5/2016,51.080002\n2/5/2016,50.389999\n25/4/2016,49.869999\n18/4/2016,51.779999\n11/4/2016,55.650002\n4/4/2016,54.419998\n28/3/2016,55.57\n21/3/2016,54.209999\n14/3/2016,53.490002\n7/3/2016,53.07\n29/2/2016,52.029999\n22/2/2016,51.299999\n16/2/2016,51.82\n8/2/2016,50.50\n1/2/2016,50.16\n25/1/2016,55.09\n19/1/2016,52.290001\n11/1/2016,50.990002\n4/1/2016,52.330002\n28/12/2015,55.48\n21/12/2015,55.669998\n14/12/2015,54.130001\n7/12/2015,54.060001\n30/11/2015,55.91\n23/11/2015,53.93\n16/11/2015,54.189999\n9/11/2015,52.84\n2/11/2015,54.919998\n26/10/2015,52.639999\n19/10/2015,52.869999\n12/10/2015,47.509998\n5/10/2015,47.110001\n28/9/2015,45.57\n21/9/2015,43.939999\n14/9/2015,43.48\n8/9/2015,43.48\n31/8/2015,42.610001\n24/8/2015,43.93\n17/8/2015,43.07\n10/8/2015,47.00\n3/8/2015,46.740002\n27/7/2015,46.700001\n20/7/2015,45.939999\n13/7/2015,46.619999\n6/7/2015,44.610001\n29/6/2015,44.400002\n22/6/2015,45.259998\n15/6/2015,46.099998\n8/6/2015,45.970001\n1/6/2015,46.139999\n26/5/2015,46.860001\n18/5/2015,46.900002\n11/5/2015,48.299999\n4/5/2015,47.75\n27/4/2015,48.66\n20/4/2015,47.869999\n13/4/2015,41.619999\n6/4/2015,41.720001\n30/3/2015,40.290001\n23/3/2015,40.970001\n16/3/2015,42.880001\n9/3/2015,41.380001\n2/3/2015,42.360001\n23/2/2015,43.849998\n17/2/2015,43.860001\n13/2/2015,43.869999\n
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //
        view = inflater.inflate(R.layout.content_detail, container, false);

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
                cursor = new CursorLoader(
                        getActivity(),
                        mQuoteUri,
                        null,
                        null,
                        null, //selection args (Symbol)
                        null
                );
            }
        }
        return cursor;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int id = loader.getId();
        switch (id){
            case QUOTE_ADAPTER:{

                //mAdapter.swapCursor(cursor);
                if(cursor.moveToFirst()){

//                    mParam1 = cursor.getString(Contract.Quote.POSITION_HISTORY);
//                    String rawHistData = mParam1;//"6/2/2017,64.00@30/1/2017,63.68@23/1/2017,65.779999@17/1/2017,62.740002@9/1/2017,62.700001@3/1/2017,62.84@27/12/2016,62.139999@19/12/2016,63.240002@12/12/2016,62.299999@5/12/2016,61.970001@28/11/2016,59.25@21/11/2016,60.529999@14/11/2016,60.349998@7/11/2016,59.02@31/10/2016,58.709999@24/10/2016,59.869999@17/10/2016,59.66@10/10/2016,57.419998@3/10/2016,57.799999@26/9/2016,57.599998@19/9/2016,57.43@12/9/2016,57.25@6/9/2016,56.209999@29/8/2016,57.669998@22/8/2016,58.029999@15/8/2016,57.619999@8/8/2016,57.939999@1/8/2016,57.959999@25/7/2016,56.68@18/7/2016,56.57@11/7/2016,53.700001@5/7/2016,52.299999@27/6/2016,51.16@20/6/2016,49.830002@13/6/2016,50.130001@6/6/2016,51.48@31/5/2016,51.790001@23/5/2016,52.32@16/5/2016,50.619999@9/5/2016,51.080002@2/5/2016,50.389999@25/4/2016,49.869999@18/4/2016,51.779999@11/4/2016,55.650002@4/4/2016,54.419998@28/3/2016,55.57@21/3/2016,54.209999@14/3/2016,53.490002@7/3/2016,53.07@29/2/2016,52.029999@22/2/2016,51.299999@16/2/2016,51.82@8/2/2016,50.50@1/2/2016,50.16@25/1/2016,55.09@19/1/2016,52.290001@11/1/2016,50.990002@4/1/2016";//,52.330002@28/12/2015,55.48@21/12/2015,55.669998@14/12/2015,54.130001@7/12/2015,54.060001@30/11/2015,55.91@23/11/2015,53.93@16/11/2015,54.189999@9/11/2015,52.84@2/11/2015,54.919998@26/10/2015,52.639999@19/10/2015,52.869999@12/10/2015,47.509998@5/10/2015,47.110001@28/9/2015,45.57@21/9/2015,43.939999@14/9/2015,43.48@8/9/2015,43.48@31/8/2015,42.610001@24/8/2015,43.93@17/8/2015,43.07@10/8/2015,47.00@3/8/2015,46.740002@27/7/2015,46.700001@20/7/2015,45.939999@13/7/2015,46.619999@6/7/2015,44.610001@29/6/2015,44.400002@22/6/2015,45.259998@15/6/2015,46.099998@8/6/2015,45.970001@1/6/2015,46.139999@26/5/2015,46.860001@18/5/2015,46.900002@11/5/2015,48.299999@4/5/2015,47.75@27/4/2015,48.66@20/4/2015,47.869999@13/4/2015,41.619999@6/4/2015,41.720001@30/3/2015,40.290001@23/3/2015,40.970001@16/3/2015,42.880001@9/3/2015,41.380001@2/3/2015,42.360001@23/2/2015,43.849998@17/2/2015,43.860001@13/2/2015,43.869999@\n";
//                    String[] parseRawData = rawHistData.split("@");
//                    //reverse string array
//                    String[] reverseParseRawData = new String[parseRawData.length];
//                    //System.out.println("Length = "+parseRawData.length);
//
//                    for(int i = 0; i < parseRawData.length; i++){
//                        reverseParseRawData[i] = parseRawData[(parseRawData.length - 1) - i];
//                    }
//
//                    LinkedHashMap<Integer, String> stockDateIndex = new LinkedHashMap<>();
//
//
//                    lineChart = (LineChart) view.findViewById(R.id.detail_line_chart);
//
//                    ArrayList<Entry> yAxesQuote = new ArrayList<>();
//                    int dataLength = reverseParseRawData.length;
//                    for(int i = 0; i <  dataLength;i++){
//                        String[] data = reverseParseRawData[i].split(",");
//                        if(!(data.length!=2)){
//                            String[] dateArray = data[0].split("/");
//                            dateArray[1] = String.valueOf(dateArray[1]);
//                            data[0] = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
//                            stockDateIndex.put(i, data[0]);
//                            yAxesQuote.add(new Entry(i,Float.valueOf(data[1])));
//                        }
//                    }
//
//
//                    LineDataSet quoteDataSet = new LineDataSet(yAxesQuote, "Quote");
//                    quoteDataSet.setColor(Color.GREEN);
//                    quoteDataSet.setDrawCircles(false);
//
//                    //lineChart.animateY(1500);
//                    lineChart.animateX(1500);
//                    lineChart.getAxisLeft().setDrawGridLines(false);
//                    lineChart.getXAxis().setDrawGridLines(false);
//                    lineChart.setPinchZoom(true);
//
//                    XAxis xAxis = lineChart.getXAxis();
//
//                    IAxisValueFormatter xAxisFormatter = new StockXAxesFormater(stockDateIndex);
//                    xAxis.setValueFormatter(xAxisFormatter);
//
//                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//                    lineChart.setData(new LineData(quoteDataSet));
//                    lineChart.invalidate();
//
                }


            }
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);

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
