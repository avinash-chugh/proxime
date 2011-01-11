package com.proxime.activities;

import com.jayway.android.robotium.solo.Solo;

/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 11/1/11
 * Time: 7:05 PM
 */
public class NewLocationActivity {
    private Solo solo;

    public NewLocationActivity(Solo solo) {
        this.solo=solo;
    }

    public void setLocationName(String locationName) {
        solo.enterText(0,locationName);
    }

    public void setLocationSpan(int span) {
        solo.enterText(1,String.valueOf(span));
    }

     public void save(){
        solo.clickOnButton("Save");
    }
}
