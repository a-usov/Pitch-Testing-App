package com.example.conal.soundrecord;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class PitchTestTest {

    Location location1;
    Location location2;
    List<Location> locations;
    LatLng latlng;



    @Before
    public void setUp() throws Exception {
        latlng = new LatLng(55.8642, -4.2518 );
        location1 = new Location(latlng);
        locations.add(location1);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkAddLocationMethod() {
        locations.add(location1);

    }

    @Test
    public void checkIsEmptyMethod() {
        System.out.println(locations.isEmpty());
        assertTrue(false == locations.isEmpty());
    }

    @Test
    public void getNumDone() {
    }

    @Test
    public void incrementNumDone() {
    }

    @Test
    public void increaseLocNumDone() {
    }

    @Test
    public void getLocations() {
    }

    @Test
    public void getLocation() {
    }
}