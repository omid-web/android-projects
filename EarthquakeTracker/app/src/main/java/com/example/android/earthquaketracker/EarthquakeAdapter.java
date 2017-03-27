package com.example.android.earthquaketracker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Omid on 3/15/2017.
 * An adapter creates a list item layout for each earthquake.
 * These item layouts will be provided to an adapter view, in our case ListView
 * to be displayed to the user.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * constructor of this adapter, we call ArrayAdapter with the given params
     *
     * @param context
     * @param earthquakes
     */
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    /**
     * Returns a list item view that display information about the earthquake at the position
     * in the list.
     *
     * @param position
     * @param convertView list item view, which contains earthquake information
     * @param parent      the list that contains all the earthquake list item views
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        /**checks if there is an existing list item view that we can reuse,
         * otherwise inflate a new one */
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,
                    parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        //finds textview of magnitude then displays the formatted magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        magnitudeView.setText(formattedMagnitude);

        ///set color of magnitude view at appropriate color
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        //find the primary location and offset location from the given current earthquake location
        String location = currentEarthquake.getLocation();
        String primaryLocation;
        String offsetLocation;

        if (location.contains(LOCATION_SEPARATOR)) {
            String[] parts = location.split(LOCATION_SEPARATOR);
            offsetLocation = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            offsetLocation = "Near the";
            primaryLocation = location;
        }

        //finds primary/offset location views then diplays the location of earthquake
        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(offsetLocation);
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        //receives time of earthquake then formats time into appropriate formatof date and time
        Date dateObject = new Date(currentEarthquake.getTimeInMilliSeconds());
        String earthquakeDate = formatDate(dateObject);
        String earthquakeTime = formatTime(dateObject);

        //finds the date/time views then displays date/time of earthquake in those textviews
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(earthquakeDate);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(earthquakeTime);

        return listItemView;
    }

    /**
     * helper function that finds the appropriate color of the given magnitude
     *
     * @param magnitude
     * @return
     */
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    /**
     * helper function that formats the double magnitude into string
     *
     * @param magnitude
     * @return
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * helper function that formats date into calendar date
     *
     * @param dateObject
     * @return
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * helper function that formats date into am/pm time
     *
     * @param dateObject
     * @return
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
