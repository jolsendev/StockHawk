package com.udacity.stockhawk.data;


import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.google.common.collect.ImmutableList;

public final class Contract {

    static final String AUTHORITY = "com.udacity.stockhawk";
    static final String PATH_QUOTE = "quote";
    static final String PATH_QUOTE_WITH_SYMBOL = "quote/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    private Contract() {
    }

    @SuppressWarnings("unused")
    public static final class Quote implements BaseColumns {

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+AUTHORITY+"/"+PATH_QUOTE;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+AUTHORITY+"/"+PATH_QUOTE_WITH_SYMBOL;
        static final String QUOTE_HISTORY_ONE_MONTH = "quote/*/one_month";
        static final String QUOTE_HISTORY_SIX_MONTH = "quote/*/six_months";
        public static final String QUOTE_HISTORY_THREE_MONTHS = "quote/*/six_months";
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_QUOTE).build();
        public static final Uri URI_SIX_MONTHS = URI.buildUpon().appendPath(QUOTE_HISTORY_SIX_MONTH).build();
        public static final Uri URI_ONE_MONTHS = URI.buildUpon().appendPath(QUOTE_HISTORY_ONE_MONTH).build();
        public static final Uri URI_THREE_MONTHS = URI.buildUpon().appendPath(QUOTE_HISTORY_THREE_MONTHS).build();
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_ABSOLUTE_CHANGE = "absolute_change";
        public static final String COLUMN_PERCENTAGE_CHANGE = "percentage_change";
        public static final String COLUMN_HISTORY = "history";
        public static final int POSITION_ID = 0;
        public static final int POSITION_SYMBOL = 1;
        public static final int POSITION_PRICE = 2;
        public static final int POSITION_ABSOLUTE_CHANGE = 3;
        public static final int POSITION_PERCENTAGE_CHANGE = 4;
        public static final int POSITION_HISTORY = 5;
        public static final ImmutableList<String> QUOTE_COLUMNS = ImmutableList.of(
                _ID,
                COLUMN_SYMBOL,
                COLUMN_PRICE,
                COLUMN_ABSOLUTE_CHANGE,
                COLUMN_PERCENTAGE_CHANGE,
                COLUMN_HISTORY
        );
        static final String TABLE_NAME = "quotes";


        public static Uri makeUriForStock(String symbol) {
            return URI.buildUpon().appendPath(symbol).build();
        }

        public static String getStockFromUri(Uri queryUri)
        {
            return queryUri.getLastPathSegment();
        }


    }

}
