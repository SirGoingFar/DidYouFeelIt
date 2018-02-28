package com.example.aolanrewaju.didyoufeelit;

/**
 * Created by a.olanrewaju on 2/27/2018.
 */

public class Event {

    private double mMagnitude;
    private String mPlace;
    private String mUrl;
    private int mFelt;
    private double mCdi;

    public Event(double mag, String place, String url, int felt, double cdi){
        mMagnitude = mag;
        mPlace = place;
        mUrl = url;
        mFelt = felt;
        mCdi = cdi;
    }

    public double getmMagnitude() {
        return mMagnitude;
    }

    public String getmPlace() {
        return mPlace;
    }

    public String getmUrl() {
        return mUrl;
    }

    public int getmFelt() {
        return mFelt;
    }

    public double getmCdi() {
        return mCdi;
    }
}
