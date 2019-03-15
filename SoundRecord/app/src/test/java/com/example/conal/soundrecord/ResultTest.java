package com.example.conal.soundrecord;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultTest {

    private Result result;

    @Before
    public void setUp() {
        result = new Result(0, 34371,34371.0 );
        result.setBounceHeight(0.8);

    }

    @Test
    public void checkGetFirstBounceMethod() {
        assertEquals(0, result.getFirstBounce());
    }

    @Test
    public void checkGetSecondBounceMethod() {
        assertEquals(34371, result.getSecondBounce());
    }

    @Test
    public void checkGetTimeOfBounceMethod() {
        assertEquals(34371.0, result.getTimeOfBounce(), 0.0);
    }

    @Test
    public void checkGetBounceHeightMethod() {
        assertEquals(0.8, result.getBounceHeight(), 0.0);
    }
}