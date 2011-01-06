package com.proxime.activities;

import android.test.ActivityInstrumentationTestCase2;
import com.jayway.android.robotium.solo.Solo;
import com.proxime.entities.Event;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.proxime.activities.EventsTest \
 * com.proxime.tests/android.test.InstrumentationTestRunner
 */
public class EventsTest extends ActivityInstrumentationTestCase2 {

    private static final String TARGET_PACKAGE_ID = "com.proxime.activities";
    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.proxime.activities.Events";


    private static Class<?> launcherActivityClass;

    static {
            try {
                launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    public EventsTest() throws ClassNotFoundException{
         super(TARGET_PACKAGE_ID, launcherActivityClass);
    }

    private Solo solo;

    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());

    }

    private String getNewEventName() {
        return "Sample " + System.currentTimeMillis();
    }

    public void testCanCreateNewEvents(){

        int previous_count = solo.getCurrentListViews().get(0).getCount();
        String eventname=getNewEventName();

        solo.clickOnButton(0);
        solo.waitForActivity("EditEvent",1000);
        assert(solo.searchText("New Event"));

        solo.enterText(0, eventname);
        solo.clickOnButton("Save");
        solo.waitForActivity("Events",1000);

        assertEquals(previous_count+1,solo.getCurrentListViews().get(0).getCount());
        Event itemAtPosition = (Event)solo.getCurrentListViews().get(0).getItemAtPosition(previous_count);
        assertEquals(eventname,itemAtPosition.getName());

    }
}
