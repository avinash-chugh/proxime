package com.proxime.infrastructure;

import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;
import com.proxime.activities.Locations;
import com.proxime.entities.Location;

import static com.proxime.infrastructure.ProximeApplication.waitTimeout;

public class LocationsActivity {
    private Solo solo;
    private ListView listView;

    public LocationsActivity(Solo solo) {
        this.solo = solo;

        solo.waitForActivity("Locations", waitTimeout);
        solo.assertCurrentActivity("The Locations activity hasn't been launched", Locations.class);
        
        this.listView = solo.getCurrentListViews().get(0);
    }

    public int getLocationsCount() {
        return listView.getCount();
    }

    public void addNewLocation() {
        solo.clickOnButton(0);
    }

    public String getLastLocationName() {
        int locationsCount = getLocationsCount();
        if (locationsCount == 0) return null;

        Location itemAtPosition = (Location) listView.getItemAtPosition(locationsCount - 1);
        return itemAtPosition.getName();
    }
}
