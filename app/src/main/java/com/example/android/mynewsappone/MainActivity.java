package com.example.android.mynewsappone;

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

    public String res = getString(R.string.myauthor);

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
    //NewsAppII
    private static final String guardian_REQUEST_URL = "https://content.guardianapis.com/search?show-tags=contributor";
    private static final String apikey = "0bd3322e-7c04-40dd-9395-e9e9064ffc37";
    @Override
    public Loader<List<MyNews>> onCreateLoader(int id, Bundle bundle) {


        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy  = sharedPrefs.getString(
                getString(R.string.settings_requestnews_by_key),
                getString(R.string.settings_order_by_default)
        );

        String numArticles = sharedPrefs.getString(getString(R.string.settings_requestnum_articles_key), getString(R.string.settings_num_articles_label));
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(guardian_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `section football``
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("page-size", numArticles);
        uriBuilder.appendQueryParameter("section", "football");
        uriBuilder.appendQueryParameter("api-key", apikey);




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




