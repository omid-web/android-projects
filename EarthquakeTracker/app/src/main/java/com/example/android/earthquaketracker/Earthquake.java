package com.example.android.earthquaketracker;

/**
 * Created by Omid on 3/15/2017.
 * An earthquake object.
 */

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliSeconds;
    private String mUrl;

    /**
     * Constructs an earthquake object with the given parameters and stores the parameters in
     * private variables.
     *
     * @param magnitude          size of magnitude
     * @param location           location of the earthquake
     * @param timeInMilliSeconds time which the earthquake occurred in milliseconds
     * @param url                website url of the earthquake
     */
    public Earthquake(double magnitude, String location, long timeInMilliSeconds, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mTimeInMilliSeconds = timeInMilliSeconds;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }

    public long getTimeInMilliSeconds() {
        return mTimeInMilliSeconds;
    }

    public String getUrl() {
        return mUrl;
    }
}
