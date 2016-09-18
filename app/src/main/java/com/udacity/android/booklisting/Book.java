package com.udacity.android.booklisting;

/**
 * Created by geovani on 14/09/16.
 */
public class Book {

    private String mTitle;
    private String mAuthor;


    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }


    public String getTitle() {
        return mTitle;
    }


    public String getAuthor() {
        return mAuthor;
    }


}
