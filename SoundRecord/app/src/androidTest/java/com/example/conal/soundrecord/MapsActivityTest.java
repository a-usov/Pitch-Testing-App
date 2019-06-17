// AUTHORS - This code was written by Artem Usov, 2296905u@student.gla.ac.uk, Olga Jodelka 2266755K@student.gla.ac.uk
// Kara Newlands 2264457n@student.gla.ac.uk, Conall Clements 2197397c@student.gla.ac.uk
// It is licensed under the GNU General Public License version 3, 29 June 2007 (https://www.gnu.org/licenses/gpl-3.0.en.html)

package com.example.conal.soundrecord;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)

public class MapsActivityTest {

    @Rule
    public IntentsTestRule<MapsActivity> rule =
            new IntentsTestRule<>(MapsActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app
        Context appContext = getTargetContext();

        assertEquals("com.example.conal.soundrecord", appContext.getPackageName());
    }

    @Test
    public void beginBtnGoesToRecordingActivity(){
        onView(withId(R.id.btnBegin)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), RecordingActivity.class)));
    }

    @Test
    public void mapDisplays() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}