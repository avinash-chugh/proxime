package com.proxime.activities;

import com.jayway.android.robotium.solo.Solo;
import com.proxime.entities.Location;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 9/1/11
 * Time: 7:40 PM
 */
public class LocationsActivity {
    private Solo solo;
    private final String activityName="activities.Locations";

    public LocationsActivity(Solo solo) {
        this.solo=solo;
    }

    public int getLocationsCount() {
        assertActivity();
        return solo.getCurrentListViews().get(0).getCount();
    }

    public void addNewLocation() {
        assertActivity();
        solo.clickOnButton(0);
    }

    private void assertActivity(){
        assertEquals(activityName,solo.getCurrentActivity().getLocalClassName());
    }


    public String getLastEventName() {

        Location itemAtPosition = (Location)solo.getCurrentListViews().get(0).getItemAtPosition(getLocationsCount()-1);
        return itemAtPosition.getName();
    }
}
