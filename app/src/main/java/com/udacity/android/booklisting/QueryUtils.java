package com.udacity.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geovani on 15/09/16.
 */
public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();


    public QueryUtils() {
    }


    public static List<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        return extractFeatureFromJson(jsonResponse);
    }


    private static List<Book> extractFeatureFromJson(String bookJSON) {
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

                String title   = volumeInfo.optString("title");
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


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream         = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10_000);
            urlConnection.setConnectTimeout(15_000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream  = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    private static URL createUrl(String newUrl) {
        URL url = null;
        try {
            url = new URL(newUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building the URL ", e);
        }
        return url;
    }



}
