// AUTHORS - This code was written by Artem Usov, 2296905u@student.gla.ac.uk, Olga Jodelka 2266755K@student.gla.ac.uk
// Kara Newlands 2264457n@student.gla.ac.uk, Conall Clements 2197397c@student.gla.ac.uk
// It is licensed under the GNU General Public License version 3, 29 June 2007 (https://www.gnu.org/licenses/gpl-3.0.en.html)

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