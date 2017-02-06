package com.udacity.stockhawk.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;
import com.udacity.stockhawk.sync.QuoteSyncJob;
import com.udacity.stockhawk.ui.AddStockDialog;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.StockAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Callback} interface
 * to handle interaction events.
 * Use the {@link StockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockFragment extends Fragment implements  LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener,
        StockAdapter.StockAdapterOnClickHandler
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int STOCK_LOADER = 0;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.recycler_view)
    RecyclerView stockRecyclerView;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.error)
    TextView error;
    private StockAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Callback mListener;

    public StockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockFragment newInstance(String param1, String param2) {
        StockFragment fragment = new StockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getLoaderManager().initLoader(STOCK_LOADER, null, this);
        adapter = new StockAdapter(getContext(), this);
        ButterKnife.bind(getActivity());
        QuoteSyncJob.initialize(getContext());

        stockRecyclerView.setAdapter(adapter);
        stockRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);


        onRefresh();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                String symbol = adapter.getSymbolAtPosition(viewHolder.getAdapterPosition());
                PrefUtils.removeStock(getContext(), symbol);
                getActivity().getContentResolver().delete(Contract.Quote.makeUriForStock(symbol), null, null);
            }
        }).attachToRecyclerView(stockRecyclerView);

        if (!checkPlayServices()) {
            // This is where we could either prompt a user that they should install
            // the latest version of Google Play Services, or add an error snackbar
            // that some features won't be available.
            Toast.makeText(getContext(), "Play services needs to be updated.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onStockFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            mListener = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Callback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swipeRefreshLayout.setRefreshing(false);

        if (data.getCount() != 0) {
            error.setVisibility(View.GONE);
        }
        adapter.setCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swipeRefreshLayout.setRefreshing(false);
        adapter.setCursor(null);
    }

    @Override
    public void onClick(String symbol) {
        Timber.d("Symbol clicked: %s", symbol);
    }

    @Override
    public void onRefresh() {

        QuoteSyncJob.syncImmediately(getContext());

        if (!MainActivity.networkUp() && adapter.getItemCount() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            String errorMessage = getString(R.string.error_no_network);
            error.setText(errorMessage);
            error.setContentDescription(errorMessage);
            error.setVisibility(View.VISIBLE);
        } else if (!MainActivity.networkUp()) {
            swipeRefreshLayout.setRefreshing(false);
            String errorMessage = getString(R.string.toast_no_connectivity);
            swipeRefreshLayout.setContentDescription(errorMessage);
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        } else if (PrefUtils.getStocks(getContext()).size() == 0) {
            swipeRefreshLayout.setRefreshing(false);
            String errorMessage = getString(R.string.error_no_stocks);
            error.setText(errorMessage);
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.GONE);
        }
    }


    public void button(@SuppressWarnings("UnusedParameters") View view) {
        new AddStockDialog().show(getActivity().getFragmentManager(), "StockDialogFragment");
    }





    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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
        void onStockFragmentInteraction(Uri uri);
    }
}
