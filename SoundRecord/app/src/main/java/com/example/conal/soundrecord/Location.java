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
    private boolean passFail = false;
    private LatLng location;
    private Timestamp time;
    private Date date;
    // Remove date and time
    private ArrayList<Float> heights;
    private Float runningAvg;

    public Location(LatLng location) {
        this.passFail = false;
        this.heights = new ArrayList<Float>();
        this.location = location;
        this.runningAvg = 0f;
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

    public Timestamp getTime(){
        return time;
    }

    public void setTime(Timestamp times){
        time = times;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date aDate){
        date = aDate;
    }

    public ArrayList<Float> getHeights() { return heights; }

    public void addHeight(Float height){ heights.add(height); }

    public Float getRunningAvg(){
        /*float total = 0f;

        for (float height : heights) {
            total += height;
        }

        return total/(heights.size());*/
        return runningAvg;
    }

    // Remove this method
    //public void setRunningAvg(Float avg) { runningAvg = avg;}

    public String toString() {
        return location + " " + heights;
    }


    protected Location(Parcel in) {
        passFail = in.readByte() != 0x00;
        location = (LatLng) in.readValue(LatLng.class.getClassLoader());
        time = (Timestamp) in.readValue(Timestamp.class.getClassLoader());
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        if (in.readByte() == 0x01) {
            heights = new ArrayList<Float>();
            in.readList(heights, Float.class.getClassLoader());
        } else {
            heights = null;
        }
        runningAvg = in.readByte() == 0x00 ? null : in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (passFail ? 0x01 : 0x00));
        dest.writeValue(location);
        dest.writeValue(time);
        dest.writeLong(date != null ? date.getTime() : -1L);
        if (heights == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(heights);
        }
        if (runningAvg == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(runningAvg);
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