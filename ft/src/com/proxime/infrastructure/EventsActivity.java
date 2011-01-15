package com.proxime.infrastructure;

import android.widget.ListView;
import com.jayway.android.robotium.solo.Solo;
import com.proxime.activities.Events;
import com.proxime.entities.Event;

import static com.proxime.infrastructure.ProximeApplication.waitTimeout;

public class EventsActivity {
    private Solo solo;
    private ListView listView;

    public EventsActivity(Solo solo) {
        this.solo = solo;

        solo.waitForActivity("Events", waitTimeout);
        solo.assertCurrentActivity("The Events activity hasn't been launched", Events.class);
        
        this.listView = solo.getCurrentListViews().get(0);
    }

    public int getEventsCount() {
        return listView.getCount();
    }

    public void addNewEvent() {
        solo.clickOnButton(0);
    }

    public String getLastEventName() {
        int eventsCount = getEventsCount();
        if (eventsCount == 0) return null;

        Event itemAtPosition = (Event) listView.getItemAtPosition(eventsCount - 1);
        return itemAtPosition.getName();
    }
}
