package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Location implements Parcelable {
    private boolean passFail;
    private LatLng location;
    private ArrayList<Float> heights;

    public Location(LatLng location) {
        this.passFail = false;
        this.heights = new ArrayList<Float>();
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

    public ArrayList<Float> getHeights() { return heights; }

    public void addHeight(Float height){ heights.add(height); }

    public Float getRunningAvg(){
        float total = 0f;

        for (float height : heights) {
            total += height;
        }

        return total/(heights.size());
    }

    public String toString() {
        return location + " " + heights;
    }


    protected Location(Parcel in) {
        passFail = in.readByte() != 0x00;
        location = (LatLng) in.readValue(LatLng.class.getClassLoader());
        if (in.readByte() == 0x01) {
            heights = new ArrayList<Float>();
            in.readList(heights, Float.class.getClassLoader());
        } else {
            heights = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (passFail ? 0x01 : 0x00));
        dest.writeValue(location);
        if (heights == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(heights);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}