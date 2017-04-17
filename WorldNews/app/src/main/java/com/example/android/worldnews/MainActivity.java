package com.example.android.worldnews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String Guardian_REQUEST_URL =
            "https://content.guardianapis.com/search";
    //https://content.guardianapis.com/search?format=json&page-size=50&from-date=2017-04-10&api-key=408d4903-9e89-40b8-8d0e-44c5135fc35d
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int NEWS_LOADER_ID = 1;

    private TextView mEmptyStateTextView;
    private ListView mListView;
    private ArticleAdapter mArticleAdapter;


    /**
     * Displays a list of articles and opens a url respective to the clicked article
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.news_list_view);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        mListView.setEmptyView(mEmptyStateTextView);
        mArticleAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        mListView.setAdapter(mArticleAdapter);

        //opens the article in a webpage after user clicks on a list item article
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = mArticleAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });

        // checks status of current active network
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText("No internet connection");
        }
    }

    /**
     * Builds a Uri from the base uri and sends a request to the articleLoader class
     * using the built uri
     * @param i
     * @param bundle
     * @return
     */
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(Guardian_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "408d4903-9e89-40b8-8d0e-44c5135fc35d");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("from-date", "2017-04-10");
        uriBuilder.appendQueryParameter("use-date", "published");

        return new ArticleLoader(this, uriBuilder.toString());
    }

    /**
     * Loader finished loading, hide the loading indicator and set empty view to
     * "no new articles found". Clear the adapter and add the new list of articles
     * to the adapter to refresh the list view
     * @param loader
     * @param articles
     */
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);

        mEmptyStateTextView.setText("No articles found");
        mArticleAdapter.clear();

        if(articles != null && !articles.isEmpty()) {
            mArticleAdapter.addAll(articles);
        }
    }

    /**
     * Loader has been reset, clear out the existing data
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mArticleAdapter.clear();
    }
}
