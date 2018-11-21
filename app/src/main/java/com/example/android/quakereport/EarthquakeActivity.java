package com.example.android.quakereport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<QuakeInfo>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final int EARTHQUAKE_LOADER_ID = 1;

    //private QuakeAdapter quakeAdapter = new QuakeAdapter(this, new ArrayList<QuakeInfo>());
    private final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public QuakeAdapter quakeAdapter;
    private TextView emptyView;
    private ProgressBar loadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: On Create EarthQuakeActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        addQuakeAdapterToListView();

        if (QueryUtils.isConnectedToNet(this)) {
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            loadingIndicator.setVisibility(View.GONE);
            emptyView.setText("No Internet Connection");
        }

        /* No more using AsyncTask
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";
        QuakeAsyncTask task = new QuakeAsyncTask();
        task.execute(url);*/
    }

    // Changed Updateui(quakesInfo) to this function.
    private void addQuakeAdapterToListView() {
        List<QuakeInfo> quakesInfo = new ArrayList<>();
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(emptyView);
        // Create a new {@link QuakeAdapter} of earthquakes
        quakeAdapter = new QuakeAdapter(this, quakesInfo);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(quakeAdapter);

        earthquakeListView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String url = ((QuakeInfo)quakeAdapter.getItem(i)).getUrl();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (browserIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public Loader<List<QuakeInfo>> onCreateLoader(int id, Bundle args) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        String limit = sharedPreferences.getString(
                getString(R.string.settings_number_of_quakes_key),
                getString(R.string.settings_number_of_quakes_default));
        Uri baseUri = Uri.parse(USGS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new QuakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<QuakeInfo>> loader, List<QuakeInfo> quakesInfo) {
        quakeAdapter.clear();
        if (quakesInfo != null && !quakesInfo.isEmpty()){
            quakeAdapter.addAll(quakesInfo);
        }
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.GONE);
        }
        if (emptyView != null) {
            emptyView.setText("No EarthQuakes Found");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<QuakeInfo>> loader) {
        quakeAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*private class QuakeAsyncTask extends AsyncTask<String, Void, ArrayList<QuakeInfo>> {

        @Override
        protected ArrayList<QuakeInfo> doInBackground(String... urls) {
            return QueryUtils.fetchQuakesInfo(urls[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<QuakeInfo> quakesInfo) {
            updateUi(quakesInfo);
        }
    }*/
}
