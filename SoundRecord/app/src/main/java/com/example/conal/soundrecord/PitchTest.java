package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PitchTest implements Parcelable {
    private List<Location> locations;
    private int numDone;

    public PitchTest() {
        this.numDone = 0;
        this.locations = new ArrayList<>();
    }

    void addLocation(Location location) {
        locations.add(location);
    }

    boolean isEmpty() {
        return locations.isEmpty();
    }

    int getNumDone() {
        return numDone;
    }

    void incrementNumDone() {
        this.numDone += 1;
    }

    void increaseLocNumDone(int index) {
        locations.get(index).incrementNumDone();
    }

    List<Location> getLocations() {
        return locations;
    }

    Location getLocation(int index) {
        return locations.get(index);
    }

    // PARCELABLE methods
    protected PitchTest(Parcel in) {
        if (in.readByte() == 0x01) {
            locations = new ArrayList<Location>();
            in.readList(locations, Location.class.getClassLoader());
        } else {
            locations = null;
        }
        numDone = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (locations == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(locations);
        }
        dest.writeInt(numDone);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PitchTest> CREATOR = new Parcelable.Creator<PitchTest>() {
        @Override
        public PitchTest createFromParcel(Parcel in) {
            return new PitchTest(in);
        }

        @Override
        public PitchTest[] newArray(int size) {
            return new PitchTest[size];
        }
    };

    public ArrayList<Double> organiseHeights() {
        ArrayList<Double> heightList = new ArrayList<>();
        int i = 0;
        for (Location loc : locations) {
            for (Result height : loc.getResults()) {
                heightList.add(i,height.getBounceHeight());
                System.out.println("At location" + locations.indexOf(loc) + " , Drop " + loc.getResults().indexOf(height));
                System.out.println("Adding height " + height.getBounceHeight());
                i++;

            }
        }


        return heightList;

    }

    public ArrayList<Double> getRunningAverages(){
        ArrayList<Double> avgHeights = new ArrayList<Double>();
        for (int i = 0; i < locations.size() ; i++){

            avgHeights.add(i,locations.get(i).getRunningAvg());
        }
        return avgHeights;

    }

    public Double getTotalAvg(){ // Averages of all the running averages
        ArrayList<Double> avgHeights = getRunningAverages();
        if(avgHeights.size()==1){
            return 0.0;
        }
        Double total = 0.0;
        for (Double height : avgHeights) {
            total += height;
        }

        return total / (avgHeights.size());

    }
}
