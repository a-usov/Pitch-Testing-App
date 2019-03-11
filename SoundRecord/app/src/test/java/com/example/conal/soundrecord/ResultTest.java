package com.example.conal.soundrecord;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultTest {

    Result result;

    @Before
    public void setUp() throws Exception {
        result = new Result(0, 34371,34371.0 );
        result.setBounceHeight(0.8);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getFirstBounce() {
        assertEquals(0, result.getFirstBounce());
    }

    @Test
    public void getSecondBounce() {
        assertEquals(34371, result.getSecondBounce());
    }

    @Test
    public void getTimeOfBounce() {
        assertTrue(34371.0 == result.getTimeOfBounce());
    }

    @Test
    public void getBounceHeight() {
        assertTrue(0.8 == result.getBounceHeight());
    }
}