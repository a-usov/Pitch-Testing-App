package com.example.conal.soundrecord;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PitchTestTest {

    PitchTest pitchTest;
    Location location1;
    Location location2;
    List<Location> locations;
    LatLng latlng;
    Result result;
    Result result2;

    @Before
    public void setUp() throws Exception {
        //Sets up a new
        pitchTest = new PitchTest();
        location1 = new Location(new LatLng(55.8642, -4.2518 ));
        result = new Result(0,0,0);
        result.setBounceHeight(64.2);
        result2 = new Result(0,0,0);
        result2.setBounceHeight(80.4);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkAddLocationMethod() {
        for (int i = 0; i < 4; i++) {
            location1.addResult(result);
        }
        pitchTest.addLocation(location1);
        assertEquals(location1, pitchTest.getLocation(0));
    }

    @Test
    public void checkIsEmptyMethod() {
        assertTrue(pitchTest.isEmpty());
    }

    @Test
    public void checkGetNumDone() {
        pitchTest.addLocation(location2);
        pitchTest.incrementNumDone();
        assertEquals(1, pitchTest.getNumDone());
    }

    @Test
    public void checkIncrementNumDone() {
        pitchTest.incrementNumDone();
        assertEquals(1,pitchTest.getNumDone());
    }

    @Test
    public void checkIncreaseLocNumDone() {
        for (int i = 0; i < 4; i++) {
            location1.addResult(result);
            location1.incrementNumDone();
        }
        pitchTest.addLocation(location1);
        pitchTest.incrementNumDone();
        pitchTest.increaseLocNumDone(0);
        assertEquals(5, pitchTest.getLocation(0).getNumDone());
    }

    @Test
    public void checkGetLocations() {
        pitchTest.addLocation(location1);
        pitchTest.addLocation(location2);
        ArrayList<Location> locs = new ArrayList<Location>();
        locs.add(location1);
        locs.add(location2);
        assertEquals(locs, pitchTest.getLocations());
    }

    @Test
    public void checkGetLocation() {
        pitchTest.addLocation(location2);
        assertEquals(location2, pitchTest.getLocation(0));
    }
}