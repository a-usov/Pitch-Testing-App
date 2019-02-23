package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Location implements Parcelable {
    private boolean passFail;
    private LatLng location;
    private List<Result> results;

    public Location(LatLng location) {
        this.passFail = false;
        this.results = new ArrayList<>();
        this.location = location;
    }

    public boolean getPassFail(){
        return passFail;
    }

    public void setPassFail(boolean bool){
        passFail = bool;
    }

    public LatLng getLocation(){
        return location;
    }

    public void setLocation(LatLng place){
        location = place;
    }

    public List<Result> getResults() { return results; }

    public void addResult(Result result){ results.add(result); }

    public int getNumLocations(){
        return results.size();
    }

    public Float getRunningAvg(){
        float total = 0f;

        for (Result result : results) {
            total += result.bounceHeight;
        }

        return total/(results.size());
    }

    public String toString() {
        return location + " " + results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.passFail ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.location, flags);
        dest.writeTypedList(this.results);
    }

    protected Location(Parcel in) {
        this.passFail = in.readByte() != 0;
        this.location = in.readParcelable(LatLng.class.getClassLoader());
        this.results = in.createTypedArrayList(Result.CREATOR);
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel source) {
            return new Location(source);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}