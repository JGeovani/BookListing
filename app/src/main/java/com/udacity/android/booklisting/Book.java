package com.udacity.android.booklisting;

/**
 * Created by geovani on 14/09/16.
 */
public class Book {

    private String mTitle;
    private String mSubTitle;


    public Book(String title, String subTitle) {
        mTitle = title;
        mSubTitle = subTitle;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }



}
