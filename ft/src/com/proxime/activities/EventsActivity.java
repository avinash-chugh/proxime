package com.proxime.activities;

import com.jayway.android.robotium.solo.Solo;
import com.proxime.entities.Event;

import static junit.framework.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 9/1/11
 * Time: 6:55 PM
 */
public class EventsActivity {
    private Solo solo;
    private final String activityName="activities.Events";

    public EventsActivity(Solo solo) {
        this.solo = solo;
    }

    public int getEventsCount() {
        assertEquals(activityName,solo.getCurrentActivity().getLocalClassName());
        return solo.getCurrentListViews().get(0).getCount();
    }

    public void addNewEvent() {
        solo.clickOnButton(0);
    }

    public String getLastEventName() {
        Event itemAtPosition = (Event)solo.getCurrentListViews().get(0).getItemAtPosition(getEventsCount()-1);
        return itemAtPosition.getName();
    }
}
