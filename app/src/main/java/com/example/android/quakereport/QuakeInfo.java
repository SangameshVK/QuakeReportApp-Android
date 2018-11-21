package com.example.android.quakereport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuakeInfo {

    private double mMagnitude;
    private String mPlace;
    private Date mDate;
    private String mUrl;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");

    public QuakeInfo(String place, double magnitude, Date date, String url) {
        mMagnitude = magnitude;
        mPlace = place;
        mDate = date;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getPlace() {
        return mPlace;
    }

    public String getFormattedDate() {
        return dateFormatter.format(mDate);
    }

    public String getFormattedTime() {
        return timeFormatter.format(mDate);
    }

    public String getUrl() { return mUrl; }
}
