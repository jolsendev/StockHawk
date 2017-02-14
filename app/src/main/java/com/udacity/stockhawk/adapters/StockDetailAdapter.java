package com.udacity.stockhawk.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jamie on 2/11/17.
 */

public class StockDetailAdapter extends RecyclerView.Adapter<StockDetailAdapter.StockDetailViewHolder> {


    private Cursor mCursor;
    private Context mContext;

    public StockDetailAdapter(Context context, Object o, int i) {
    }

    public void setCursor(Cursor cursor){
        this.mCursor = cursor;
    }
    @Override
    public StockDetailAdapter.StockDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StockDetailAdapter.StockDetailViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class StockDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public StockDetailViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }

//        @BindView(R.id.symbol)
//        TextView symbol;
//
//        @BindView(R.id.price)
//        TextView price;
//
//        @BindView(R.id.change)
//        TextView change;
//
//        StockViewHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            int adapterPosition = getAdapterPosition();
//            cursor.moveToPosition(adapterPosition);
//            int symbolColumn = cursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
//            clickHandler.onClick(cursor.getString(symbolColumn));
//
//        }


    }
}
