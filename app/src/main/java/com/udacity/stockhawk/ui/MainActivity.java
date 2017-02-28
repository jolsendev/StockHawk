package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.fragments.StockDetailFragment;
import com.udacity.stockhawk.fragments.StockFragment;

public class MainActivity extends AppCompatActivity implements StockFragment.Callback
{

    public static final String QUOTE_DATE_RANGE = "pref_quote_date_range";
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
        if(findViewById(R.id.detail_layout)!=null){
            mTwoPane = true;

        }else{
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_activity_settings, menu);
//        MenuItem item = menu.findItem(R.id.action_change_units);
//        setDisplayModeMenuItemIcon(item);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addStock(String symbol) {
        StockFragment sf = (StockFragment) getSupportFragmentManager().findFragmentById(R.id.stock_fragment);
        sf.addStock(symbol);
    }

    @Override
    public void onStockFragmentInteraction(Uri uri, int position) {
        if(mTwoPane){
            //launch fragment
        }else{
            //create intent
            Intent intent = new Intent(this, DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(StockDetailFragment.POSITION, position);
            intent.setData(uri);
            intent.putExtras(bundle);
            startActivity(intent);
        }

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
