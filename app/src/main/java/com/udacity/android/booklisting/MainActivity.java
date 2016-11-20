package com.udacity.android.booklisting;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?";
    private static final int BOOK_LOADER_ID = 1;

    // UI
    private ListView mListView;
    //
    private BookAdapter mAdapter;
    //
    private ArrayList<Book> bookArrayList;
    private String mQuery;
    private TextView mEmptyText;
    private View mIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mIndicator = findViewById(R.id.loading_indicator);
        //
        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString("query");
        }
        //
        bookArrayList = new ArrayList<>();
        mAdapter = new BookAdapter(this, bookArrayList);
        //
        mListView = (ListView) findViewById(R.id.listViewBooks);
        mListView.setAdapter(mAdapter);
        //
        mEmptyText = (TextView) findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyText);
        //
        if (QueryUtils.isInternetAccess(MainActivity.this)) {
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        } else {
            mEmptyText.setText(R.string.no_internet_connection);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i(TAG, "onFocusChange");
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                Log.i(TAG, "onQueryTextSubmit | mQuery: " + mQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Log.i(TAG, "onQueryTextChange | searchQuery: " + searchQuery);
                assert searchQuery != null;
                assert bookArrayList != null;
                if (QueryUtils.isInternetAccess(getBaseContext())) {
                    mAdapter.filter(searchQuery.trim(), bookArrayList);
                    mListView.invalidate();
                    //
                    String newFilter = !TextUtils.isEmpty(searchQuery) ? searchQuery : null;
                    Log.i(TAG, "newFilter: " + newFilter);
                    if (mQuery == null && newFilter == null) {
                        return true;
                    }
                    if (mQuery != null && mQuery.equals(newFilter)) {
                        return true;
                    }
                    mQuery = newFilter;
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    Snackbar.make(searchView, R.string.snackbar_no_internet, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    mEmptyText.setText(R.string.no_internet_connection);
                }
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Log.i(TAG, "home");
            mListView.setEmptyView(mEmptyText);
            mEmptyText.setText(R.string.empty_welcome);
            return true;
        }
        else
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(BASE_BOOKS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //
        mIndicator.setVisibility(View.VISIBLE);
        //
        if (mQuery == null) {
            mQuery = "android";
        }
        Log.i(TAG, "onCreateLoader | mQuery: " + mQuery);
        uriBuilder.appendQueryParameter("q", mQuery);
        uriBuilder.appendQueryParameter("maxResults", "40");
        //
        Log.i(TAG, "onCreateLoader | uriBuilder.toString(): " + uriBuilder.toString());
        return  new BookLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> dataBooks) {
        Log.i(TAG, "onLoadFinished");
        //
        mIndicator.setVisibility(View.GONE);
        //
        if (dataBooks != null && !dataBooks.isEmpty()) {
            mAdapter.addAll(dataBooks);
            bookArrayList.addAll(dataBooks);
        } else {
            mListView.setEmptyView(mEmptyText);
            mEmptyText.setText(getResources().getString(R.string.no_book));
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(TAG, "onLoaderReset");
        mAdapter.clear();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: " + mQuery);
        outState.putString("query", mQuery);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstanceState: " + mQuery);
        mQuery = savedInstanceState.getString("query");
    }



}
