package com.udacity.stockhawk.ui;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.fragments.StockFragment;
import com.udacity.stockhawk.sync.QuoteSyncJob;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class MainActivity extends AppCompatActivity implements StockFragment.Callback
{
    private boolean mTwoPane;
    public static String STOCK_FRAGMENT = "sf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(new DumperPluginsProvider() {
                    @Override
                    public Iterable<DumperPlugin> get() {
                        return new Stetho.DefaultDumperPluginsBuilder(getApplicationContext())
                                .provide(new MyDumperPlugin())
                                .finish();
                    }
                })
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        setContentView(R.layout.activity_main);
        if(findViewById(R.id.stock_detail_container)!=null){
            mTwoPane = true;

        }else{
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
        MenuItem item = menu.findItem(R.id.action_change_units);
        setDisplayModeMenuItemIcon(item);
        return true;
    }

    private void setDisplayModeMenuItemIcon(MenuItem item) {
        if (PrefUtils.getDisplayMode(this)
                .equals(getString(R.string.pref_display_mode_absolute_key))) {
            item.setIcon(R.drawable.ic_percentage);
        } else {
            item.setIcon(R.drawable.ic_dollar);
        }
    }

    public void addStock(String symbol) {
        StockFragment sf = (StockFragment) getSupportFragmentManager().findFragmentById(R.id.stock_fragment);
        sf.addStock(symbol);
    }

    @Override
    public void onStockFragmentInteraction(Uri uri) {

    }


    public void button(@SuppressWarnings("UnusedParameters") View view) {
        new AddStockDialog().show(getFragmentManager(), "StockDialogFragment");
    }

    private class MyDumperPlugin implements DumperPlugin {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public void dump(DumperContext dumpContext) throws DumpException {

        }
    }

}
