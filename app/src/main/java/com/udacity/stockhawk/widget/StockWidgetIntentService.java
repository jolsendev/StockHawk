package com.udacity.stockhawk.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_ABSOLUTE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_HISTORY;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PERCENTAGE_CHANGE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_PRICE;
import static com.udacity.stockhawk.data.Contract.Quote.COLUMN_SYMBOL;

/**
 * Created by a5w5nzz on 3/15/2017.
 */

public class StockWidgetIntentService extends IntentService {
    public static final int POSITION_ID = 0;
    public static final int POSITION_SYMBOL = 1;
    public static final int POSITION_PRICE = 2;
    public static final int POSITION_ABSOLUTE_CHANGE = 3;
    public static final int POSITION_PERCENTAGE_CHANGE = 4;
    public static final int POSITION_HISTORY = 5;

    public static final String[] QUOTE_COLUMNS = {
            Contract.Quote._ID,
            COLUMN_SYMBOL,
            COLUMN_PRICE,
            COLUMN_ABSOLUTE_CHANGE,
            COLUMN_PERCENTAGE_CHANGE,
            COLUMN_HISTORY
    };

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StockWidgetIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockWidgetProvider.class));

        // Get Stock's data from the ContentProvider
        Cursor cursor = getContentResolver().query(
                Contract.Quote.URI,
                QUOTE_COLUMNS,
                null,
                null,
                null);

        // Extract the Stock's data from the Cursor
        String symbol = cursor.getString(POSITION_SYMBOL);
        // Perform this loop procedure for each Stock's widget

        for (int appWidgetId : appWidgetIds){
            int layoutId = R.layout.stock_widget_small;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews

            // Create an Intent to launch MainActivity

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }

    }
//    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
//    private void setRemoteContentDescription(RemoteViews views, String description) {
//        views.setContentDescription(R.id.widget_icon, description);
//    }
}
