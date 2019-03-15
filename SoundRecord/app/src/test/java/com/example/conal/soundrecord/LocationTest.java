package com.example.conal.soundrecord;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class LocationTest {
    private Location location;
    private Result result;
    private List<Result> list;
    private LatLng latlng;

    @Before
    public void setUp() {
        latlng = new LatLng(55.8642, -4.2518 );
        location = new Location(latlng);
        result = new Result(0, 34371,34371.0 );
        result.setBounceHeight(0.8);
        for(int i=0; i<5; i++){
            location.addResult(result);
        }
    }

    @Test
    public void checkGetResultsMethod(){
        list = Arrays.asList(result,result, result, result, result);
        assertEquals(list, location.getResults());
    }

    @Test
    public void checkAddResultMethod(){
        list = Arrays.asList(result,result,result, result, result, result);
        location.addResult(result);
        assertEquals(list, location.getResults());
    }

    @Test
    public void checkDeleteResultMethod(){
        list = Arrays.asList(result,result, result, result);
        location.deleteResult();
        assertEquals(list, location.getResults());
    }

    @Test
    public void checkGetLocationMethod(){
        assertEquals(latlng, location.getLocation());
    }

    @Test
    public  void checkGetRunningAvgMethod(){
        assertEquals(0.8, location.getRunningAvg(), 0.0);
    }

    @Test
    public void checkGetNumDoneMethod(){
        int numDone = location.getNumDone();
        assertEquals(0, numDone);
    }

    @Test
    public void checkIncrementNumDone(){
        location.incrementNumDone();
        assertEquals(1, location.getNumDone());
    }
}