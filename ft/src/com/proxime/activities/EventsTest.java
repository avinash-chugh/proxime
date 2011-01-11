package com.proxime.activities;

import android.test.ActivityInstrumentationTestCase2;

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

    private static Class<?> launcherActivityClass;

    static {
            try {
                launcherActivityClass = Class.forName(ProximeApplication.LAUNCHER_ACTIVITY_FULL_CLASSNAME);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    public EventsTest() throws ClassNotFoundException{

         super(ProximeApplication.TARGET_PACKAGE_ID, launcherActivityClass);
    }

    private ProximeApplication application;

    protected void setUp() throws Exception {
        application = new ProximeApplication(getInstrumentation(), getActivity());

    }

    private String getNewName(String name) {
        return "SampleEvent " + System.currentTimeMillis();
    }

    public void testCanCreateNewEvents(){

        EventsActivity eventsActivity = application.getEventsActivity();
        int previousCount=eventsActivity.getEventsCount();
        eventsActivity.addNewEvent();
        NewEventActivity newEventActivity = application.getNewEventActivity();


        String eventname=getNewName("SampleEvent ");
        newEventActivity.setEventname(eventname);
        newEventActivity.save();

        eventsActivity= application.getEventsActivity();
        assertEquals(previousCount+1,eventsActivity.getEventsCount());
        assertEquals(eventname,eventsActivity.getLastEventName());

    }
    public void testCanCreateNewLocation(){
        application.openLocationActivity();
        LocationsActivity locationsActivity = application.getLocationsActivity();
        int previousCount  = locationsActivity.getLocationsCount();
        locationsActivity.addNewLocation();
        NewLocationActivity newLocationActivity = application.getNewLocationActivity();

        String locationName=getNewName("SampleLocation ");
        newLocationActivity.setLocationName(locationName);
        newLocationActivity.setLocationSpan(10);
        newLocationActivity.save();

        locationsActivity=application.getLocationsActivity();
        assertEquals(previousCount+1,locationsActivity.getLocationsCount());
        assertEquals(locationName,locationsActivity.getLastEventName());
    }
}

