package com.udacity.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by geovani on 15/09/16.
 */
public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mURL;


    public BookLoader(Context context, String url) {
        super(context);
        mURL = url;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Book> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        return QueryUtils.fetchBookData(mURL);
    }



}
