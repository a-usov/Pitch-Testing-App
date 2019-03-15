package com.example.conal.soundrecord;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FinalActivityTest {

    private Location loc;

    @Before
    public void setUp() {
        loc = new Location(new LatLng(55.8642, -4.2518));
        for (int i = 0; i < 5; i++) {
            Result res = new Result(0,0,0);
            res.setBounceHeight(60.82);
            loc.addResult(res);
        }
    }

    @Test
    public void checkLocationToString() {
        assertEquals("55.8642,-4.2518,60.82,60.82,60.82,60.82,60.82,", loc.toString());
    }
}