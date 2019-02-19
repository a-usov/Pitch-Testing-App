package com.example.conal.soundrecord;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public  class Result implements Parcelable {
    List<Double> sound;
    int firstBounce;
    int secondBounce;
    double timeOfBounce;
    double bounceHeight;

    public Result(List<Double> sound, int firstBounce, int secondBounce, double timeOfBounce) {
        this.sound = sound;
        this.firstBounce = firstBounce;
        this.secondBounce = secondBounce;
        this.timeOfBounce = timeOfBounce;
    }

    public void setBounceHeight(double bounceHeight) {
        this.bounceHeight = bounceHeight;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.sound);
        dest.writeInt(this.firstBounce);
        dest.writeInt(this.secondBounce);
        dest.writeDouble(this.timeOfBounce);
        dest.writeDouble(this.bounceHeight);
    }

    protected Result(Parcel in) {
        this.sound = new ArrayList<Double>();
        in.readList(this.sound, Double.class.getClassLoader());
        this.firstBounce = in.readInt();
        this.secondBounce = in.readInt();
        this.timeOfBounce = in.readDouble();
        this.bounceHeight = in.readDouble();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel source) {
            return new Result(source);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
