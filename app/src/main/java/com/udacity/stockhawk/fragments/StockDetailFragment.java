package com.udacity.stockhawk.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.common.collect.ImmutableList;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.chart_helpers.StockXAxesFormater;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.udacity.stockhawk.ui.DetailActivity.POSITION;

public class StockDetailFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match

//    private static final int QUOTE_ADAPTER = 0;

    private static final String ARG_PARAM1 = "param1";
//    public static final String POSITION = "position";
    public static final String PREF_ONE_MONTH = "one_month";
    public static final String PREF_SIX_MONTHS = "six_months";
    public static final String PREF_ONE_YEAR = "one_year";
    public static final String PREF_THREE_MONTHS = "three_months";

    private ImmutableList<String> projection;
    private View view;
    private int position;
    private String stock;
    private ArrayList<Entry> yAxesQuote;
    private LinkedHashMap<Integer, String> stockDateIndex;
    private int prefRange;
    private Uri mUri;
    private String mSymbol;
    private String sharedPref;
    private String mHistory;

    private View lineChartView;

    private LineChart lineChart;
    private TextView stockTextView;
    private TextView tvOneMonthButton;
    private TextView tvThreeMonthButton;
    private TextView tvSixMonth;
    private TextView tvOneYear;
    private double mPrice;
    private double mAbsChange;
    private double mPrecChange;

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

        //Get all of the values for the fragment
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
        projection = Contract.Quote.QUOTE_COLUMNS;
        Bundle extra  = getActivity().getIntent().getExtras();
        setPosition(extra.getInt(POSITION));

        //setActionToolBar

        if (getArguments() != null) {
//            public static final int POSITION_ID = 0;
//            public static final int POSITION_SYMBOL = 1;
            mSymbol = getArguments().getString(Contract.Quote.COLUMN_SYMBOL);
//            public static final int POSITION_PRICE = 2;
            mPrice = getArguments().getDouble(Contract.Quote.COLUMN_PRICE);
//            public static final int POSITION_ABSOLUTE_CHANGE = 3;
            mAbsChange = getArguments().getDouble(Contract.Quote.COLUMN_ABSOLUTE_CHANGE);
//            public static final int POSITION_PERCENTAGE_CHANGE = 4;
            mPrecChange = getArguments().getDouble(Contract.Quote.COLUMN_PERCENTAGE_CHANGE);
//            public static final int POSITION_HISTORY = 5;
            mHistory = getArguments().getString(Contract.Quote.COLUMN_HISTORY);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getLoaderManager().initLoader(QUOTE_ADAPTER, null, this);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "HERE IN FRAG", Toast.LENGTH_SHORT).show();

        setHasOptionsMenu(false);

        View view = inflater.inflate(R.layout.detail_stock_chart, container, false);
        lineChart = (LineChart)view.findViewById(R.id.detail_line_chart);
        stockTextView = (TextView)view.findViewById(R.id.stock_text_view);
        tvOneMonthButton = (TextView) view.findViewById(R.id.tv_one_month);
        tvThreeMonthButton = (TextView)view.findViewById(R.id.tv_three_months);
        tvSixMonth = (TextView)view.findViewById(R.id.tv_six_months);
        tvOneYear = (TextView)view.findViewById(R.id.tv_one_year);

        sharedPref = PrefUtils.getDateRangePreference(getContext());

        switch(sharedPref){

            case StockDetailFragment.PREF_ONE_MONTH:{
                prefRange = 30;
                break;
            }
            case StockDetailFragment.PREF_SIX_MONTHS:{
                prefRange = 180;
                break;
            }
            case StockDetailFragment.PREF_ONE_YEAR:{
                prefRange = 360;
                break;
            }
            case StockDetailFragment.PREF_THREE_MONTHS:{
                prefRange = 90;
                break;
            }
            default:
                break;
        }

        stockTextView.setText(mSymbol);

        String[] splitRawData = mHistory.split("@");
        String[] reverseParseRawData = reverseData(splitRawData);

        stockDateIndex = new LinkedHashMap<>();

        yAxesQuote = new ArrayList<>();

        ArrayList<Entry> yAxesQuote = getEntries(stockDateIndex, reverseParseRawData);

        //
        LineDataSet quoteDataSet = new LineDataSet(yAxesQuote, "Quote");
        quoteDataSet.setColor(Color.GREEN);
        quoteDataSet.setDrawCircles(false);

        //holder.lineChart.animateY(15(00);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setPinchZoom(false);


        XAxis xAxis = lineChart.getXAxis();

        IAxisValueFormatter xAxisFormatter = new StockXAxesFormater(stockDateIndex);
        xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(new LineData(quoteDataSet));
        lineChart.invalidate();



        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ArrayList<Entry> getEntries(LinkedHashMap<Integer, String> stockDateIndex, String[] reverseParseRawData) {
        int dataLength = reverseParseRawData.length;
        ArrayList<Entry> retEntry = new ArrayList<>();
        for(int i = 0; i <  dataLength;i++){
            String[] data = reverseParseRawData[i].split(",");
            if(!(data.length!=2)){
                String[] dateArray = data[0].split("/");
                dateArray[1] = String.valueOf(dateArray[1]);
                data[0] = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
                if(dataInPrefDateRange(data)){
                    stockDateIndex.put(i, data[0]);
                    retEntry.add(new Entry(i,Float.valueOf(data[1])));
                }
            }
        }
        return retEntry;
    }

    private boolean dataInPrefDateRange(String[] data) {
        //get Prefered date range
        //if data[0] is in range return true
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = inputDateFormat.parse(data[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -prefRange);
        Date dateBefore30Days = cal.getTime();

        if(date.after(dateBefore30Days)){
            return true;
        }else{return false;}
    }

    private String[] reverseData(String[] splitRawData) {

        String[] retData = new String[splitRawData.length];
        for(int i = 0; i < splitRawData.length; i++){
            retData[i] = splitRawData[(splitRawData.length - 1) - i];
        }
        return retData;
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
                    PrefUtils.setDateRangePreference(getContext(), StockDetailFragment.PREF_ONE_MONTH);
                    resetButtonColor();
                    tvOneMonthButton.setTextColor(Color.RED);
                    break;
                }
                case R.id.tv_three_months:{
                    mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("three_months").build();
                    PrefUtils.setDateRangePreference(getContext(), StockDetailFragment.PREF_THREE_MONTHS);
                    resetButtonColor();
                    break;
                }
                case R.id.tv_six_months:{
                    mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("six_months").build();
                    PrefUtils.setDateRangePreference(getContext(), StockDetailFragment.PREF_SIX_MONTHS);
                    resetButtonColor();
                    break;
                }
                case R.id.tv_one_year:{
                    mUri = Contract.Quote.URI;
                    PrefUtils.setDateRangePreference(getContext(), StockDetailFragment.PREF_ONE_YEAR);
                    resetButtonColor();
                    break;
                }
            }
        }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void resetButtonColor() {
        tvOneMonthButton.setTextColor(getContext().getColor(R.color.textColor));
        tvOneYear.setTextColor(getContext().getColor(R.color.textColor));;
        tvSixMonth.setTextColor(getContext().getColor(R.color.textColor));;
        tvThreeMonthButton.setTextColor(getContext().getColor(R.color.textColor));;
    }
}


