package com.proxime.infrastructure;

import android.app.Activity;
import android.app.Instrumentation;
import com.jayway.android.robotium.solo.Solo;

public class ProximeApplication {
    private Solo solo;

    public static final int waitTimeout = 2000;
    private Activity activity;

    public ProximeApplication(Instrumentation instrumentation, Activity activity) {
        this.activity = activity;
        solo = new Solo(instrumentation, activity);
    }

    public EventsActivity getEventsActivity() {
         return new EventsActivity(solo);
    }

    public EditEventActivity getEditEventActivity() {
        return new EditEventActivity(solo);
    }

    public void openLocationActivity() {
        solo.clickOnMenuItem("Locations");
    }

    public LocationsActivity getLocationsActivity() {
        return new LocationsActivity(solo);
    }

    public EditLocationActivity getEditLocationActivity() {
        return new EditLocationActivity(solo);
    }

    public void Close() {
        try {
            solo.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        activity.finish();
    }
}