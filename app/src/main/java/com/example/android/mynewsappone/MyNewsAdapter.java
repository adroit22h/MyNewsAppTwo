package com.example.android.mynewsappone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyNewsAdapter extends ArrayAdapter<MyNews> {

    public MyNewsAdapter(Context context, ArrayList<MyNews> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_card_view, parent, false);
        }

        MyNews currentNews = getItem(position);

        TextView newsArticleTitle = (TextView) listItemView.findViewById(R.id.article_title);
        String title = currentNews.getArticleTitle();
        newsArticleTitle.setText(title);


        TextView newsArticleSectionName = (TextView) listItemView.findViewById(R.id.article_section_name);
        String category = currentNews.getArticleSectionName();
        newsArticleSectionName.setText(category);

        TextView newsArticlePubdate = (TextView) listItemView.findViewById(R.id.article_pubdate);
        String date = currentNews.getArticlePubdate();
        newsArticlePubdate.setText(date);

        TextView newsArticleAuthorName = (TextView) listItemView.findViewById(R.id.article_author_name);
        String author = currentNews.getArticleAuthorName();
        newsArticleAuthorName.setText(author);

        TextView newsArticleUrl = (TextView) listItemView.findViewById(R.id.article_url);
        String url = currentNews.getArticleUrl();
        newsArticleUrl.setText(url);

        return listItemView;
    }


}