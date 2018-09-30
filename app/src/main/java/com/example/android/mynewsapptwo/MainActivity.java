package com.example.android.mynewsapptwo;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MyNews>> {
    private MyNewsAdapter mAdapter;
    private TextView mNoContentTextView;

    /** URL for news data from the Guardian API */
    private static final String REQUEST_URL =
            "https://content.guardianapis.com/search?show-tags=contributor&api-key=0bd3322e-7c04-40dd-9395-e9e9064ffc37";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = findViewById(R.id.myNewsListItem);
        mNoContentTextView = findViewById(R.id.view_empty1);
        newsListView.setEmptyView(mNoContentTextView);
        mAdapter = new MyNewsAdapter(this, new ArrayList<MyNews>());

        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MyNews currentNews = mAdapter.getItem(position);

                //changed getmArticleUrl to getArticleUrl
                Uri newsUri = Uri.parse(currentNews.getArticleUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {


            View loadingIndicator = findViewById(R.id.myprogressbar);
            loadingIndicator.setVisibility(View.GONE);
            mNoContentTextView.setText(R.string.error_message);
        }
    }

//    @Override
////    public Loader<List<MyNews>> onCreateLoader(int id, Bundle bundle) {
////        return new MyNewsLoader(this, REQUEST_URL);
////    }

    @Override
    public Loader<List<MyNews>> onCreateLoader(int id, Bundle bundle) {
        //return new MyNewsLoader(this, REQUEST_URL);



        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy  = sharedPrefs.getString(
                getString(R.string.news_order_key),
                getString(R.string.main_order)
        );

        String totalArticles = sharedPrefs.getString(getString(R.string.news_total_articles), getString(R.string.news_sort_label));
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse("https://content.guardianapis.com/search?show-tags=contributor&api-key=0bd3322e-7c04-40dd-9395-e9e9064ffc37");

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `q=music``
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", totalArticles);
        uriBuilder.appendQueryParameter("q", "music");
//        uriBuilder.appendQueryParameter("api-key", apiKey);
//        Log.i(LOG_TAG, uriBuilder.toString());
        return new MyNewsLoader(this, uriBuilder.toString());

    }






    @Override
    public void onLoadFinished(Loader<List<MyNews>> loader, List<MyNews> news) {
        View loadingIndicator = findViewById(R.id.myprogressbar);
        loadingIndicator.setVisibility(View.GONE);
        mNoContentTextView.setText(R.string.error_message);
        mAdapter.clear();

        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MyNews>> loader) {
        mAdapter.clear();
    }


    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Options Menu we specified in XML
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
}