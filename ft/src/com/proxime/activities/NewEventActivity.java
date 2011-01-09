package com.proxime.activities;

import com.jayway.android.robotium.solo.Solo;

/**
 * Created by IntelliJ IDEA.
 * User: Jijesh Mohan
 * Date: 9/1/11
 * Time: 7:23 PM
 */
public class NewEventActivity {
    private Solo solo;

    public NewEventActivity(Solo solo) {
        this.solo = solo;
    }

    public void setEventname(String eventname) {
        solo.enterText(0,eventname);
    }
    public void save(){
        solo.clickOnButton("Save");
    }
}
