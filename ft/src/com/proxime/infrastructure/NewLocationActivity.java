package com.proxime.infrastructure;

import com.jayway.android.robotium.solo.Solo;

/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 17/1/11
 * Time: 2:19 PM
 */
public class NewLocationActivity {
    private Solo solo;

    public NewLocationActivity(Solo solo) {
        this.solo = solo;

    }


    public void setLocationName(String locationName) {
        solo.enterText(1,locationName);
    }

    public void save() {
        solo.clickOnButton("OK");
    }
}
