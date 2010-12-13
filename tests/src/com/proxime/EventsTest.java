package com.proxime;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.proxime.EventsTest \
 * com.proxime.tests/android.test.InstrumentationTestRunner
 */
public class EventsTest extends ActivityInstrumentationTestCase2<Events> {

    public EventsTest() {
        super("com.proxime", Events.class);
    }

}
