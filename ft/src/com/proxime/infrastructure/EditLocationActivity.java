package com.proxime.infrastructure;

import com.jayway.android.robotium.solo.Solo;
import com.proxime.activities.EditLocation;

import static com.proxime.infrastructure.ProximeApplication.waitTimeout;

public class EditLocationActivity {
    private Solo solo;

    public EditLocationActivity(Solo solo) {
        this.solo = solo;

        solo.waitForActivity("EditLocation", waitTimeout);
        solo.assertCurrentActivity("The EditLocation activity hasn't been launched", EditLocation.class);
    }

    public void setLocationName(String locationName) {
        solo.enterText(0, locationName);
    }

    public void setLocationSpan(int span) {
        solo.enterText(1, String.valueOf(span));
    }

    public void save() {
        solo.clickOnButton("Save");
    }
}
