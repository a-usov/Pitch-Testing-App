package com.example.conal.soundrecord;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.text.InputType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withInputType;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertEquals;

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
        //onView(withId(R.id.scrollView)).perform(scrollTo());
        onView(withId(R.id.btnSubmitForm)).perform(scrollTo(), click());
        intended(hasComponent(new ComponentName(getTargetContext(), MapsActivity.class)));
    }

    @Test
    public void testValidContract() {
        onView(withId(R.id.contract)).perform(click(), typeText("hi"));
        //onView(withId(R.id.btnSubmitForm)).perform(scrollTo(), click());
        //intended();

        onView(withId(R.id.contract)).check(matches(allOf(withInputType(InputType.TYPE_CLASS_TEXT))));
    }

    @Test
    public void backBtnGoesToHomeActivity() {
        pressBack();
        intended(hasComponent(new ComponentName(getTargetContext(), HomeActivity.class)));
    }
}