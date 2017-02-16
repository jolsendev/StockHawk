package com.udacity.stockhawk.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jamie on 2/11/17.
 */

public class StockDetailAdapter extends RecyclerView.Adapter<StockDetailAdapter.StockDetailViewHolder> {


    private Cursor mCursor;
    private Context mContext;
    private String mParam1;
    private int count;

    public StockDetailAdapter(Context context, Object o, int i) {
        this.mContext = context;
    }

    public void setCursor(Cursor cursor){
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public StockDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_stock_chart, parent, false);
        return new StockDetailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockDetailAdapter.StockDetailViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        String symbol = mCursor.getString(Contract.Quote.POSITION_SYMBOL);
        holder.stockTextView.setText(symbol);
        mParam1 = mCursor.getString(Contract.Quote.POSITION_HISTORY);
        String rawHistData = mParam1;//"6/2/2017,64.00@30/1/2017,63.68@23/1/2017,65.779999@17/1/2017,62.740002@9/1/2017,62.700001@3/1/2017,62.84@27/12/2016,62.139999@19/12/2016,63.240002@12/12/2016,62.299999@5/12/2016,61.970001@28/11/2016,59.25@21/11/2016,60.529999@14/11/2016,60.349998@7/11/2016,59.02@31/10/2016,58.709999@24/10/2016,59.869999@17/10/2016,59.66@10/10/2016,57.419998@3/10/2016,57.799999@26/9/2016,57.599998@19/9/2016,57.43@12/9/2016,57.25@6/9/2016,56.209999@29/8/2016,57.669998@22/8/2016,58.029999@15/8/2016,57.619999@8/8/2016,57.939999@1/8/2016,57.959999@25/7/2016,56.68@18/7/2016,56.57@11/7/2016,53.700001@5/7/2016,52.299999@27/6/2016,51.16@20/6/2016,49.830002@13/6/2016,50.130001@6/6/2016,51.48@31/5/2016,51.790001@23/5/2016,52.32@16/5/2016,50.619999@9/5/2016,51.080002@2/5/2016,50.389999@25/4/2016,49.869999@18/4/2016,51.779999@11/4/2016,55.650002@4/4/2016,54.419998@28/3/2016,55.57@21/3/2016,54.209999@14/3/2016,53.490002@7/3/2016,53.07@29/2/2016,52.029999@22/2/2016,51.299999@16/2/2016,51.82@8/2/2016,50.50@1/2/2016,50.16@25/1/2016,55.09@19/1/2016,52.290001@11/1/2016,50.990002@4/1/2016";//,52.330002@28/12/2015,55.48@21/12/2015,55.669998@14/12/2015,54.130001@7/12/2015,54.060001@30/11/2015,55.91@23/11/2015,53.93@16/11/2015,54.189999@9/11/2015,52.84@2/11/2015,54.919998@26/10/2015,52.639999@19/10/2015,52.869999@12/10/2015,47.509998@5/10/2015,47.110001@28/9/2015,45.57@21/9/2015,43.939999@14/9/2015,43.48@8/9/2015,43.48@31/8/2015,42.610001@24/8/2015,43.93@17/8/2015,43.07@10/8/2015,47.00@3/8/2015,46.740002@27/7/2015,46.700001@20/7/2015,45.939999@13/7/2015,46.619999@6/7/2015,44.610001@29/6/2015,44.400002@22/6/2015,45.259998@15/6/2015,46.099998@8/6/2015,45.970001@1/6/2015,46.139999@26/5/2015,46.860001@18/5/2015,46.900002@11/5/2015,48.299999@4/5/2015,47.75@27/4/2015,48.66@20/4/2015,47.869999@13/4/2015,41.619999@6/4/2015,41.720001@30/3/2015,40.290001@23/3/2015,40.970001@16/3/2015,42.880001@9/3/2015,41.380001@2/3/2015,42.360001@23/2/2015,43.849998@17/2/2015,43.860001@13/2/2015,43.869999@\n";
        String[] parseRawData = rawHistData.split("@");
        String[] reverseParseRawData = new String[parseRawData.length];

        for(int i = 0; i < parseRawData.length; i++){
            reverseParseRawData[i] = parseRawData[(parseRawData.length - 1) - i];
        }

        LinkedHashMap<Integer, String> stockDateIndex = new LinkedHashMap<>();

        //
        ArrayList<Entry> yAxesQuote = new ArrayList<>();
        int dataLength = reverseParseRawData.length;
        for(int i = 0; i <  dataLength;i++){
            String[] data = reverseParseRawData[i].split(",");
            if(!(data.length!=2)){
                String[] dateArray = data[0].split("/");
                dateArray[1] = String.valueOf(dateArray[1]);
                data[0] = dateArray[1]+"/"+dateArray[0]+"/"+dateArray[2];
                stockDateIndex.put(i, data[0]);
                yAxesQuote.add(new Entry(i,Float.valueOf(data[1])));
            }
        }

        //
        LineDataSet quoteDataSet = new LineDataSet(yAxesQuote, "Quote");
        quoteDataSet.setColor(Color.GREEN);
        quoteDataSet.setDrawCircles(false);

        holder.lineChart.animateY(1500);
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

    @Override
    public int getItemCount()
    {
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
        }
        return count;
    }

    class StockDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.detail_line_chart)
        LineChart lineChart;

        @BindView(R.id.stock_text_view)
        TextView stockTextView;

        public StockDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
