package com.udacity.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by geovani on 14/09/16.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public List<Book> parkingList;
    ArrayList<Book> arraylist;
    Context mContext;


    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        mContext    = context;
        parkingList = books;
        arraylist   = new ArrayList<>();
        arraylist.addAll(parkingList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        titleText.setText(currentBook.getTitle());

        TextView subtitleText = (TextView) listItemView.findViewById(R.id.subtitle);
        subtitleText.setText(currentBook.getSubTitle());

        return listItemView;
    }


    @Override
    public int getCount() {
        return parkingList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        parkingList.clear();
        if (charText.length() == 0) {
            parkingList.addAll(arraylist);
        } else {
            for (Book bookDetail : arraylist) {
                if (charText.length() != 0 &&
                    bookDetail.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    parkingList.add(bookDetail);
                } else if (charText.length() != 0 &&
                           bookDetail.getSubTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    parkingList.add(bookDetail);
                }
            }
        }
        notifyDataSetChanged();
    }



}
