package com.udacity.android.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
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

    private List<Book> mBooks;
    private ArrayList<Book> auxBooks;
    private Context mContext;


    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        mContext = context;
        mBooks   = books;
        auxBooks = new ArrayList<>();
        auxBooks.addAll(mBooks);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleText = (TextView) listItemView.findViewById(R.id.title);
        assert currentBook != null;
        titleText.setText(currentBook.getTitle());

        TextView subtitleText = (TextView) listItemView.findViewById(R.id.subtitle);
        subtitleText.setText(currentBook.getAuthor());

        return listItemView;
    }


    @Override
    public int getCount() {
        if (mBooks != null) {
            return mBooks.size();
        } else {
            return 0;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public void filter(String charText, List<Book> books) {
        charText = charText.toLowerCase(Locale.getDefault());
        books.clear();
        if (charText.length() == 0) {
            books.addAll(auxBooks);
        } else {
            for (Book book : auxBooks) {
                if (charText.length() != 0 &&
                    book.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    books.add(book);
                } else if (charText.length() != 0 &&
                           book.getAuthor().toLowerCase(Locale.getDefault()).contains(charText)) {
                    books.add(book);
                }
            }
        }
        notifyDataSetChanged();
    }



}
