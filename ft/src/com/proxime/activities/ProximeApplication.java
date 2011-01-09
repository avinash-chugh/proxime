package com.proxime.activities;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;


/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 9/1/11
 * Time: 6:43 PM
 */
class ProximeApplication {


    private Solo solo;

    public static final String TARGET_PACKAGE_ID = "com.proxime.activities";
    public static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.proxime.activities.Events";
    public static final String EVENTS_ACTIVITY = "Events" ;
    public static final String NEW_EVENT_ACTIVITY = "EditEvent" ;
    public static final String LOCATION_ACTIVITY = "Locations" ;

    public ProximeApplication(Instrumentation inst,Activity activity) throws ClassNotFoundException{
        solo = new Solo(inst,activity);
    }

    public void waitForActivity(String activity) {
        assertTrue("Failed to load activity:" + activity, solo.waitForActivity(activity, 1000));

    }

    public EventsActivity getEventsActivity() {
        waitForActivity(ProximeApplication.EVENTS_ACTIVITY);
         return new EventsActivity(solo);

    }

    public NewEventActivity getNewEventActivity() {
       waitForActivity(ProximeApplication.NEW_EVENT_ACTIVITY);
        return new NewEventActivity(solo);
    }

    public void openLocationActivity() {
        solo.clickOnMenuItem("Locations");
    }

    public LocationsActivity getLocationsActivity() {
        waitForActivity(ProximeApplication.LOCATION_ACTIVITY);
        return new LocationsActivity(solo);
    }
}