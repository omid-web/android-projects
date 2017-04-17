package com.example.android.worldnews;

import android.text.TextUtils;
import android.util.Log;
import android.util.StringBuilderPrinter;

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

import static android.R.id.input;

/**
 * Created by Omid on 4/16/2017.
 */

class QueryUtil {
    private static final String LOG_TAG = QueryUtil.class.getName();
    
    private QueryUtil() {
        
    }

    /**
     * Function that uses helper methods to return the list of articles
     * @param requestUrl
     * @return
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the http request", e);
        }
        
        List<Article> articles = extractFeatureFromJson(jsonResponse);
        return articles;
    }

    /**
     * Creates a url from the string
     * @param requestUrl
     * @return
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with building the url", e);
        }
        Log.i(LOG_TAG, requestUrl);
        return url;
    }

    /**
     * Takes a url, creates a connection, stores the response and returns the response
     * @param url
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.d(LOG_TAG, url.toString());
                Log.e(LOG_TAG, "Error in response code " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem in retrieving the json results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        Log.i(LOG_TAG, jsonResponse);
        return jsonResponse;
    }

    /**
     * helper method for the makeHttpRequest that processes the inputstream into a string
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *
     * @param jsonResponse
     * @return
     */
    private static List<Article> extractFeatureFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        Log.i(LOG_TAG, jsonResponse);
        List<Article> articles = new ArrayList<Article>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject articleJsonResponse = baseJsonResponse.getJSONObject("response");
            JSONArray articleArray = articleJsonResponse.getJSONArray("results");
            int arrayLength = articleArray.length();

            for (int i = 0; i < arrayLength; i++) {
                JSONObject currentArticle = articleArray.getJSONObject(i);
                String title = currentArticle.getString("webTitle");
                String section = currentArticle.getString("sectionName");
                String date = currentArticle.getString("webPublicationDate");
                String url = currentArticle.getString("webUrl");

                Article article = new Article(title, section, date, url);
                articles.add(article);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the json response");
        }

        return articles;
    }


}
