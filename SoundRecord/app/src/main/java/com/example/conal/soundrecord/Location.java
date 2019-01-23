package com.example.conal.soundrecord;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Location {
    private boolean passFail = false;
    private LatLng location;
    private Timestamp time;
    private Date date;
    private ArrayList<Float> heights;
    private Float runningAvg;


    public Location(boolean passFail, LatLng location, Timestamp time, Date date, ArrayList<Float> heights, Float runningAvg) {
        this.passFail = passFail;
        this.location = location;
        this.time = time;
        this.date = date;
        this.time = new Timestamp(date.getTime());
        this.heights = heights;
        this.runningAvg = runningAvg;

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

    public Float getRunningAvg(){return runningAvg;}

    public void setRunningAvg(Float avg) { runningAvg = avg;}



}
