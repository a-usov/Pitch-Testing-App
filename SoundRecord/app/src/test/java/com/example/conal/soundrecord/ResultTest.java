// AUTHORS - This code was written by Artem Usov, 2296905u@student.gla.ac.uk, Olga Jodelka 2266755K@student.gla.ac.uk
// Kara Newlands 2264457n@student.gla.ac.uk, Conall Clements 2197397c@student.gla.ac.uk
// It is licensed under the GNU General Public License version 3, 29 June 2007 (https://www.gnu.org/licenses/gpl-3.0.en.html)

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