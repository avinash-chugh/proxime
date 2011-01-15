package com.proxime.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.Smoke;
import com.proxime.activities.Events;
import com.proxime.infrastructure.EditLocationActivity;
import com.proxime.infrastructure.EventsActivity;
import com.proxime.infrastructure.LocationsActivity;
import com.proxime.infrastructure.EditEventActivity;
import com.proxime.infrastructure.ProximeApplication;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProximeTest extends ActivityInstrumentationTestCase2<Events> {
    private ProximeApplication application;

    public ProximeTest() {
        super("com.proxime.activities", Events.class);
    }

    @Override
    protected void setUp() throws Exception {
        application = new ProximeApplication(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        application.Close();
        super.tearDown();
    }

    @Smoke
    public void testCanCreateNewEvent() {
        EventsActivity eventsActivity = application.getEventsActivity();
        int previousCount = eventsActivity.getEventsCount();
        eventsActivity.addNewEvent();

        EditEventActivity editEventActivity = application.getEditEventActivity();
        String eventName = newEvent();
        editEventActivity.setEventName(eventName);
        editEventActivity.save();

        eventsActivity = application.getEventsActivity();
        assertThat(eventsActivity.getEventsCount(), is(equalTo(previousCount + 1)));
        assertThat(eventsActivity.getLastEventName(), is(equalTo(eventName)));
    }

    @Smoke
    public void testCanCreateNewLocation() {
        application.openLocationActivity();
        LocationsActivity locationsActivity = application.getLocationsActivity();
        int previousCount = locationsActivity.getLocationsCount();
        locationsActivity.addNewLocation();

        EditLocationActivity editLocationActivity = application.getEditLocationActivity();
        String locationName = newLocation();
        editLocationActivity.setLocationName(locationName);
        editLocationActivity.setLocationSpan(10);
        editLocationActivity.save();

        locationsActivity = application.getLocationsActivity();
        assertThat(locationsActivity.getLocationsCount(), is(equalTo(previousCount + 1)));
        assertThat(locationsActivity.getLastLocationName(), is(equalTo(locationName)));
    }

    private String newEvent() {
        return String.format("Sample event %d", System.currentTimeMillis());
    }

    private String newLocation() {
        return String.format("Sample location %d", System.currentTimeMillis());
    }
}

