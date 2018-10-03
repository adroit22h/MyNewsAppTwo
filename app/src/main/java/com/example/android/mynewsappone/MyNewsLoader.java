package com.example.android.mynewsappone;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MyNewsLoader extends AsyncTaskLoader<List<MyNews>> {


    private String mUrl;

    public MyNewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MyNews> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<MyNews> newsList = MyNewsQueryUtils.fetchNewsData(mUrl);
        return newsList;
    }
}