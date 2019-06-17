// AUTHORS - This code was written by Artem Usov, 2296905u@student.gla.ac.uk, Olga Jodelka 2266755K@student.gla.ac.uk
// Kara Newlands 2264457n@student.gla.ac.uk, Conall Clements 2197397c@student.gla.ac.uk
// It is licensed under the GNU General Public License version 3, 29 June 2007 (https://www.gnu.org/licenses/gpl-3.0.en.html)

package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Location implements Parcelable {
    private final LatLng location;
    private final List<Result> results;
    private int numDone;

    public Location(LatLng location) {
        this.results = new ArrayList<>();
        this.location = location;
        this.numDone = 0;
    }

    public Location() {
        this.results = new ArrayList<>();
        this.location = null;
        this.numDone = 0;
    }

    List<Result> getResults() {
        return results;
    }

    void addResult(Result result) {
        results.add(result);
    }

    void deleteResult() {
        results.remove(results.size() - 1);
    }

    // location is possibly null when we can't get a GPS signal
    LatLng getLocation() throws NullPointerException {
        return location;
    }

    int getNumDone() {
        return numDone;
    }

    void incrementNumDone() {
        this.numDone += 1;
    }

    Double getRunningAvg() {
        double total = 0.0;

        for (Result result : results) {
            total += result.getBounceHeight();
        }

        return total / (results.size());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();

        s.append(getLocation().latitude).append(",").append(getLocation().longitude).append(",");
        for (int i = 0; i < 5; i++) {
            s.append(getResults().get(i));
            s.append(",");
        }
        return s.toString();
    }

    // PARCELABLE METHODS
    Location(Parcel in) {
        location = (LatLng) in.readValue(LatLng.class.getClassLoader());
        if (in.readByte() == 0x01) {
            results = new ArrayList<>();
            in.readList(results, Result.class.getClassLoader());
        } else {
            results = null;
        }
        numDone = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(location);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(numDone);
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
