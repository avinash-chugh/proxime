package com.proxime.activities;


import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import com.proxime.R;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.shadows.ShadowDialog;
import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.view.TestMenu;
import com.xtremelabs.robolectric.view.TestMenuItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class EventsTest {
    private Events events;
    private TestMenu menu;

    @Before
    public void setUp() throws Exception {
        events = new Events();
        events.onCreate(null);

        menu = new TestMenu();
        events.onCreateOptionsMenu(menu);
    }

    @After
    public void tearDown() throws Exception {
        events.finish();
    }

    @Test
    public void canRetrieveMenus() {
        assertThat(menu.size(), is(equalTo(3)));
        assertMenuItem(menu, 0, "@string/menu_locations");
        assertMenuItem(menu, 1, "@string/menu_add_event");
        assertMenuItem(menu, 2, "@string/menu_about");
    }

    @Test
    public void canSwitchToLocationsView() {
        events.onOptionsItemSelected(menu.getItem(0));
        assertNextActivity(Locations.class);
    }

    @Test
    public void canLaunchNewEventView() {
        events.onOptionsItemSelected(menu.getItem(1));
        assertNextActivity(EditEvent.class);
    }

    @Test
    public void canLaunchNewEventViewOnClickingTheAddButton() {
        Button button = (Button) events.findViewById(R.id.createButton);
        assertThat(button, is(not(nullValue())));

        button.performClick();
        assertNextActivity(EditEvent.class);
    }

    @Test
    public void canShowAboutDialog() {
        events.onOptionsItemSelected(menu.getItem(2));
        ShadowActivity activity = shadowOf(events);
        assertThat(activity.getNextDialog(), is(equalTo(R.id.about_proxime)));

        ShadowDialog dialog = shadowOf(events.onCreateDialog(R.id.about_proxime, null));
        assertThat(dialog.getTitle().toString(), is(equalTo("About Proxime")));
    }

    private void assertMenuItem(TestMenu menu, int index, String title) {
        TestMenuItem testMenuItem = (TestMenuItem) menu.getItem(index);
        assertThat(testMenuItem.getTitle().toString(), is(equalTo(title)));
    }

    private void assertNextActivity(Class<? extends Activity> activityClass) {
        Intent intent = shadowOf(events).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(intent);
        assertThat(shadowIntent.getComponent().getClassName(), is(equalTo(activityClass.getName())));
    }
}
