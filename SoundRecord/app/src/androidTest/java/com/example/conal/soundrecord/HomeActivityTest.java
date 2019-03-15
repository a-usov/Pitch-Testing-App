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

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HomeActivityTest {

    @Rule
    public IntentsTestRule<HomeActivity> rule =
            new IntentsTestRule<>(HomeActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app
        Context appContext = getTargetContext();

        assertEquals("com.example.conal.soundrecord", appContext.getPackageName());
    }

    @Test
    public void footballBtnGoesToFormActivity(){
        onView(withId(R.id.footballBtn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), FormPDFActivity.class)));
    }

    @Test
    public void hockeyBtnGoesToFormActivity() {
        onView(withId(R.id.hockeyBtn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), FormPDFActivity.class)));
    }

    @Test
    public void rugbyBtnGoesToFormActivity() {
        onView(withId(R.id.rugbyBtn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), FormPDFActivity.class)));
    }

    @Test
    public void tennisBtnGoesToFormActivity() {
        onView(withId(R.id.tennisBtn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), FormPDFActivity.class)));
    }

    @Test
    public void concreteGoesToMapsActivity() {
        onView(withId(R.id.concreteBtn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));
    }
}


