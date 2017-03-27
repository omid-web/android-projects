package com.example.android.earthquaketracker;

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
 * Created by Omid on 3/20/2017.
 * QueryUtil contains helper methods for making a URL request and retrieving result as a string
 */

public class QueryUtil {
    private static final String LOG_TAG = QueryUtil.class.getSimpleName();

    private QueryUtil() {

    }

    /**
     * using helper functions fetch the list of earthquakes from the string request url.
     *
     * @param requestUrl
     * @return
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the http request.", e);
        }

        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);
        return earthquakes;
    }

    /**
     * create a valid url from the string url
     *
     * @param requestUrl
     * @return
     */
    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with building the url ", e);
        }
        return url;
    }

    /**
     * Make an http request to the given url and return the response as string
     *
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
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake json results.", e);
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

    /**
     * Convert the inputstream into a string which contains the whole json respsonse
     *
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
     * returns a list of earthquakes built from parsing the String jsonResponse
     *
     * @param jsonResponse
     * @return
     */
    private static List<Earthquake> extractFeatureFromJson(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) return null;

        List<Earthquake> earthquakes = new ArrayList<Earthquake>();
        //parse the json response string, if theres a problem print the error message to the logs
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            int arrayLength = earthquakeArray.length();

            //for each eathquake in the array creat an earthquake object
            for (int i = 0; i < arrayLength; i++) {
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                earthquakes.add(earthquake);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the json", e);
        }
        return earthquakes;
    }
}
