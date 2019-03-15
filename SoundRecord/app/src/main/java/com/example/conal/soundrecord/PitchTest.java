package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PitchTest implements Parcelable {
    private final List<Location> locations;
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
    private PitchTest(Parcel in) {
        if (in.readByte() == 0x01) {
            locations = new ArrayList<>();
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

    public List<Double> organiseHeights() {
        List<Double> heightList = new ArrayList<>();
        for (Location loc : locations) {
            for (Result height : loc.getResults()) {
                heightList.add(height.getBounceHeight());
            }
        }

        return heightList;
    }

    public List<Double> getRunningAverages() {
        List<Double> avgHeights = new ArrayList<>();
        for (Location loc : locations) {
            avgHeights.add(loc.getRunningAvg());
        }

        return avgHeights;
    }

    public Double getTotalAvg() { // Averages of all the running averages
        List<Double> avgHeights = getRunningAverages();

        double total = 0.0;

        for (double height : avgHeights) {
            total += height;
        }

        return total / (avgHeights.size());
    }
}

