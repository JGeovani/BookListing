package com.udacity.android.booklisting;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // UI
    private ListView mListView;
    //
    private BookAdapter mAdapter;
    private ArrayList<Book> bookArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        bookArrayList = new ArrayList<>();
        setupInitData(bookArrayList);
        mAdapter = new BookAdapter(this, bookArrayList);
        //

        mListView = (ListView) findViewById(R.id.listViewBooks);
        mListView.setAdapter(mAdapter);
    }


    private void setupInitData(ArrayList list) {
        list.add(new Book("Sonho Lúcidos", "Um guia para aprender a controlar seus sonhos"));
        list.add(new Book("De Zero a Um", "Aprenda empreendedorismo com o Vale do Silício"));
        list.add(new Book("A Arte de Fazer Acontecer", ""));
        list.add(new Book("A Única Coisa", "Como o foco pode trazer resultados extraordinários"));
        list.add(new Book("A Estratégia do Oceano Azul", ""));
        list.add(new Book("A Ciência de Ficar Rico", ""));
        list.add(new Book("Os Segredos de Uma Mente Milionária", ""));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchQuery) {
                mAdapter.filter(searchQuery.trim());//
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



}
