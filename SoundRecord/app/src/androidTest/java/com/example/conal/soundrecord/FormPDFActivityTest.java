package com.example.conal.soundrecord;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ScrollView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)

public class FormPDFActivityTest {

    @Rule
    public IntentsTestRule<FormPDFActivity> rule =
            new IntentsTestRule<>(FormPDFActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app
        Context appContext = getTargetContext();

        assertEquals("com.example.conal.soundrecord", appContext.getPackageName());
    }

    @Test
    public void submitFormBtnGoesToFormActivity(){
        onView(withId(R.id.textInput)).perform(swipeUp(),swipeUp(),swipeUp(),swipeUp());
        onView(withId(R.id.btnSubmitForm)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));
    }
}