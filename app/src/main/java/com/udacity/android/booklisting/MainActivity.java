package com.udacity.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookArrayList = new ArrayList<>();
//        setupInitData(bookArrayList);

        if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString("query");
        }

        mAdapter = new BookAdapter(this, bookArrayList);

        mListView = (ListView) findViewById(R.id.listViewBooks);
        mListView.setAdapter(mAdapter);

        mEmptyText = (TextView) findViewById(R.id.empty_view);
        mListView.setEmptyView(mEmptyText);
        //
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            showLoading();
            mEmptyText.setText(R.string.no_internet_connection);
        }

        getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }


    private void showLoading() {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);
    }


    @Deprecated
    private void setupInitData(ArrayList list) {
        list.add(new Book("Sonho Lúcido", "Dylan Tuccillo, Jared Zeizel, Thomas Peisel"));
        list.add(new Book("De Zero a Um", "Peter Thiel"));
        list.add(new Book("A Arte de Fazer Acontecer", "David Allen"));
        list.add(new Book("A Única Coisa", "Jay Papasan, Gary Keller"));
        list.add(new Book("A Estratégia do Oceano Azul", "W. Chan Kim e Renée Mauborgne"));
        list.add(new Book("A Ciência de Ficar Rico", "Wallace D. Wattles"));
        list.add(new Book("Os Segredos de Uma Mente Milionária", "T. Harv Eker"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                Log.i(TAG, "onQueryTextChange | searchQuery: " + searchQuery);
                assert searchQuery != null;
                mAdapter.filter(searchQuery.trim());
                mListView.invalidate();
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // Do something when expanded
                return true;  // Return true to expand action view
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        showLoading();
        //
        if (mQuery == null) {
            mQuery = "android";
        }
        Log.i(TAG, "onCreateLoader | mQuery: " + mQuery);
        uriBuilder.appendQueryParameter("q", mQuery);
        //
        uriBuilder.appendQueryParameter("maxResults", "40");
        //
        Log.i(TAG, "onCreateLoader | uriBuilder.toString(): " + uriBuilder.toString());
        return  new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> dataBooks) {
        Log.i(TAG, "onLoadFinished");
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

//        mAdapter.clear();
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
        mQuery = savedInstanceState.getString("query");
        Log.i(TAG, "onRestoreInstanceState: " + mQuery);
    }



}
