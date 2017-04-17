package com.example.android.worldnews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omid on 4/16/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        //checks if there is an existing list item view we can reuse, otherwise inflate a new one
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, parent, false);
        }
        Article currentArticle = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_title);
        titleTextView.setText(currentArticle.getTitle());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.aticle_section);
        sectionTextView.setText(currentArticle.getSection());

        TextView dateView = (TextView) listItemView.findViewById(R.id.article_date);
        dateView.setText(formatDate(currentArticle.getDate()));

        return listItemView;
    }

    /**
     *
     * @param date example "2014-02-17T12:05:47Z"
     * @return "2014-02-17"
     */
    private String formatDate(String date) {
        return date.substring(0, 10);
    }
}
