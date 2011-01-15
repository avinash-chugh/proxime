package com.proxime.infrastructure;

import com.jayway.android.robotium.solo.Solo;
import com.proxime.activities.EditEvent;

import static com.proxime.infrastructure.ProximeApplication.waitTimeout;

public class EditEventActivity {
    private Solo solo;

    public EditEventActivity(Solo solo) {
        this.solo = solo;

        solo.waitForActivity("EditEvent", waitTimeout);
        solo.assertCurrentActivity("The EditEvent activity hasn't been launched", EditEvent.class);
    }

    public void setEventName(String eventName) {
        solo.enterText(0, eventName);
    }

    public void save() {
        solo.clickOnButton("Save");
    }
}
