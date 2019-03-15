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

@RunWith(AndroidJUnit4.class)

public class FinalActivityTest {

    @Rule
    public IntentsTestRule<FinalActivity> rule =
            new IntentsTestRule<>(FinalActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app
        Context appContext = getTargetContext();

        assertEquals("com.example.conal.soundrecord", appContext.getPackageName());
    }

    @Test
    public void discardTestBtnGoesToHomeActivity(){
        onView(withId(R.id.discard_test_btn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), HomeActivity.class)));
    }

    @Test
    public void newTestBtnGoesToFormPDFActivity() {
        onView(withId(R.id.new_test_btn)).perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), FormPDFActivity.class)));
    }
}