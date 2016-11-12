package com.udacity.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geovani on 12/11/16.
 */

public class GoogleBooksAPI {

    private final static String TAG = GoogleBooksAPI.class.getSimpleName();


    static List<Book> extractFeatureFromJson(String bookJSON) {
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(bookJSON);
            int totalItems = root.optInt("totalItems");
            if (totalItems == 0) {
                Log.i(TAG, "No Result Found :(");
                return null;
            }
            //
            JSONArray items = root.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONObject currentBook = items.optJSONObject(i);
                JSONObject volumeInfo = currentBook.optJSONObject("volumeInfo");

                String title = volumeInfo.optString("title");
                String authors = volumeInfo.optString("authors");
                Log.i(TAG, "title: " + title + " by " + "authors: " + authors);

                Book book = new Book(title, authors);
                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }



}
