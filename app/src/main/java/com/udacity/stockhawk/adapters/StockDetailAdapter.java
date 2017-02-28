package com.udacity.stockhawk.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.chart_helpers.StockXAxesFormater;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.fragments.StockDetailFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jamie on 2/11/17.
 */

public class StockDetailAdapter extends RecyclerView.Adapter<StockDetailAdapter.StockDetailViewHolder> {


    private static final String BUTTON_SELECT_PREFERENCE = "button_select_preference";
    private final StockDetailFragment mSDF;
    private Cursor mCursor;
    private static Context mContext;
    private String mParam1;
    private int count;
    private ArrayList<Entry> yAxesQuote;
    private LinkedHashMap<Integer, String> stockDateIndex;
    private int prefRange;
    private Uri mUri;
    private String mSymbol;
    private String sharedPref;


    public StockDetailAdapter(Context context, Object o, int i, StockDetailFragment stockDetailFragment) {
        this.mContext = context;
        mSDF = stockDetailFragment;

    }

    public void setCursor(Cursor cursor){
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public StockDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_stock_chart, parent, false);



        sharedPref = PrefUtils.getDateRangePreference(mContext);


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
        return new StockDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockDetailAdapter.StockDetailViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        mSymbol = mCursor.getString(Contract.Quote.POSITION_SYMBOL);



        holder.stockTextView.setText(mSymbol);
        mParam1 = mCursor.getString(Contract.Quote.POSITION_HISTORY);
        String rawHistData = mParam1;//"6/2/2017,64.00@30/1/2017,63.68@23/1/2017,65.779999@17/1/2017,62.740002@9/1/2017,62.700001@3/1/2017,62.84@27/12/2016,62.139999@19/12/2016,63.240002@12/12/2016,62.299999@5/12/2016,61.970001@28/11/2016,59.25@21/11/2016,60.529999@14/11/2016,60.349998@7/11/2016,59.02@31/10/2016,58.709999@24/10/2016,59.869999@17/10/2016,59.66@10/10/2016,57.419998@3/10/2016,57.799999@26/9/2016,57.599998@19/9/2016,57.43@12/9/2016,57.25@6/9/2016,56.209999@29/8/2016,57.669998@22/8/2016,58.029999@15/8/2016,57.619999@8/8/2016,57.939999@1/8/2016,57.959999@25/7/2016,56.68@18/7/2016,56.57@11/7/2016,53.700001@5/7/2016,52.299999@27/6/2016,51.16@20/6/2016,49.830002@13/6/2016,50.130001@6/6/2016,51.48@31/5/2016,51.790001@23/5/2016,52.32@16/5/2016,50.619999@9/5/2016,51.080002@2/5/2016,50.389999@25/4/2016,49.869999@18/4/2016,51.779999@11/4/2016,55.650002@4/4/2016,54.419998@28/3/2016,55.57@21/3/2016,54.209999@14/3/2016,53.490002@7/3/2016,53.07@29/2/2016,52.029999@22/2/2016,51.299999@16/2/2016,51.82@8/2/2016,50.50@1/2/2016,50.16@25/1/2016,55.09@19/1/2016,52.290001@11/1/2016,50.990002@4/1/2016";//,52.330002@28/12/2015,55.48@21/12/2015,55.669998@14/12/2015,54.130001@7/12/2015,54.060001@30/11/2015,55.91@23/11/2015,53.93@16/11/2015,54.189999@9/11/2015,52.84@2/11/2015,54.919998@26/10/2015,52.639999@19/10/2015,52.869999@12/10/2015,47.509998@5/10/2015,47.110001@28/9/2015,45.57@21/9/2015,43.939999@14/9/2015,43.48@8/9/2015,43.48@31/8/2015,42.610001@24/8/2015,43.93@17/8/2015,43.07@10/8/2015,47.00@3/8/2015,46.740002@27/7/2015,46.700001@20/7/2015,45.939999@13/7/2015,46.619999@6/7/2015,44.610001@29/6/2015,44.400002@22/6/2015,45.259998@15/6/2015,46.099998@8/6/2015,45.970001@1/6/2015,46.139999@26/5/2015,46.860001@18/5/2015,46.900002@11/5/2015,48.299999@4/5/2015,47.75@27/4/2015,48.66@20/4/2015,47.869999@13/4/2015,41.619999@6/4/2015,41.720001@30/3/2015,40.290001@23/3/2015,40.970001@16/3/2015,42.880001@9/3/2015,41.380001@2/3/2015,42.360001@23/2/2015,43.849998@17/2/2015,43.860001@13/2/2015,43.869999@\n";
        // create LinkedHashMap of the values for the last 6 months.

        String[] splitRawData = rawHistData.split("@");
        String[] reverseParseRawData = reverseData(splitRawData);

        stockDateIndex = new LinkedHashMap<>();

        yAxesQuote = new ArrayList<>();

        ArrayList<Entry> yAxesQuote = getEntries(stockDateIndex, reverseParseRawData);

        //
        LineDataSet quoteDataSet = new LineDataSet(yAxesQuote, "Quote");
        quoteDataSet.setColor(Color.GREEN);
        quoteDataSet.setDrawCircles(false);

        //holder.lineChart.animateY(1500);
        holder.lineChart.getAxisLeft().setDrawGridLines(false);
        holder.lineChart.getXAxis().setDrawGridLines(false);
        holder.lineChart.setPinchZoom(true);

        XAxis xAxis = holder.lineChart.getXAxis();

        IAxisValueFormatter xAxisFormatter = new StockXAxesFormater(stockDateIndex);
        xAxis.setValueFormatter(xAxisFormatter);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        holder.lineChart.setData(new LineData(quoteDataSet));
        holder.lineChart.invalidate();
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

    @Override
    public int getItemCount()
    {
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
        }
        return count;
    }



    class StockDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.detail_line_chart)
        LineChart lineChart;

        @BindView(R.id.stock_text_view)
        TextView stockTextView;

        @BindView(R.id.btn_one_month)
        Button btnOneMonthButton;

        @BindView(R.id.btn_three_months)
        Button btnThreeMonthButton;

        @BindView(R.id.btn_six_months)
        Button btnSixMonth;

        @BindView(R.id.btn_one_year)
        Button btnOneYear;

        public StockDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
            btnOneMonthButton.setOnClickListener(this);
            btnOneYear.setOnClickListener(this);
            btnThreeMonthButton.setOnClickListener(this);
            btnSixMonth.setOnClickListener(this);
            setPrefRangeButton();

        }

        private void setPrefRangeButton() {
            if(sharedPref != null){
                switch (sharedPref){
                    case StockDetailFragment.PREF_ONE_MONTH:{
                        btnOneMonthButton.setTextColor(Color.RED);
                        break;
                    }
                    case StockDetailFragment.PREF_THREE_MONTHS:{
                        btnThreeMonthButton.setTextColor(Color.RED);
                        break;
                    }
                    case StockDetailFragment.PREF_SIX_MONTHS:{
                        btnSixMonth.setTextColor(Color.RED);
                        break;
                    }
                    case StockDetailFragment.PREF_ONE_YEAR:{
                        btnOneYear.setTextColor(Color.RED);
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
                case R.id.btn_one_month:{
                    mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("one_month").build();
                    PrefUtils.setDateRangePreference(mContext, StockDetailFragment.PREF_ONE_MONTH);
                    mSDF.setPosition(getAdapterPosition());
                    resetButtonColor();
                    btnOneMonthButton.setTextColor(Color.RED);
                    mSDF.restartLoader();
                    //Toast.makeText(mContext, "One month btn", Toast.LENGTH_LONG).show();
                    break;
                }
                case R.id.btn_three_months:{
                    mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("three_months").build();
                    PrefUtils.setDateRangePreference(mContext, StockDetailFragment.PREF_THREE_MONTHS);
                    mSDF.setPosition(getAdapterPosition());
                    resetButtonColor();
                    //btnThreeMonthButton.setTextColor(Color.RED);
                    mSDF.restartLoader();
                    break;
                }
                case R.id.btn_six_months:{
                    mUri = Contract.Quote.URI.buildUpon().appendPath("date_range").appendPath("six_months").build();
                    PrefUtils.setDateRangePreference(mContext, StockDetailFragment.PREF_SIX_MONTHS);
                    mSDF.setPosition(getAdapterPosition());
                    resetButtonColor();
                    //btnSixMonth.setTextColor(Color.RED);
                    mSDF.restartLoader();
                    break;
                }
                case R.id.btn_one_year:{
                    mUri = Contract.Quote.URI;
                    PrefUtils.setDateRangePreference(mContext, StockDetailFragment.PREF_ONE_YEAR);
                    resetButtonColor();
                    //btnOneYear.setTextColor(Color.RED);
                    mSDF.setPosition(getAdapterPosition());
                    mSDF.restartLoader();
                    break;
                }
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void resetButtonColor() {
            btnOneMonthButton.setTextColor(mContext.getColor(R.color.textColor));
            btnOneYear.setTextColor(mContext.getColor(R.color.textColor));;
            btnSixMonth.setTextColor(mContext.getColor(R.color.textColor));;
            btnThreeMonthButton.setTextColor(mContext.getColor(R.color.textColor));;
        }
    }

    public interface Callback {
        // TODO: Update argument type and name
        void restartLoader();
    }
}
