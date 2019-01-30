package com.example.conal.soundrecord;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class PitchTest {
    private String name;
    private ArrayList<Location> locations;
    private Date startDate;
    private Time time;

    public PitchTest(String name, ArrayList<Location> locations, Date startDate, Time time) {
        this.name = name;
        this.locations = locations;
        this.startDate = startDate;
        this.time = time;
    }

    public void addLocation(Location location){
        locations.add(location);
    }

    public void deleteTest(Location location){
        locations.remove(location);
    }

    public boolean isEmpty(){
        if (locations.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Location> getLocations(){ return locations;}

    public void setLocations(ArrayList<Location> locations) { this.locations = locations;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
