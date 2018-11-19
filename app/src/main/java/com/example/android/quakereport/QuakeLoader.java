package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class QuakeLoader extends AsyncTaskLoader<List<QuakeInfo>> {

    private static final String LOG_TAG = QuakeLoader.class.getName();

    private String mUrl;
    public QuakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<QuakeInfo> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchQuakesInfo(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
