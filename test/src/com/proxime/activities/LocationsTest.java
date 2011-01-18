package com.proxime.activities;


import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;
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
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LocationsTest {
    private Locations locations;
    private TestMenu menu;

    @Before
    public void setUp() throws Exception {
        locations = new Locations();
        locations.onCreate(null);

        menu = new TestMenu();
        locations.onCreateOptionsMenu(menu);
    }

    @After
    public void tearDown() throws Exception {
        locations.finish();
    }

    @Test
    public void canRetrieveMenus() {
        assertThat(menu.size(), is(equalTo(3)));
        assertMenuItem(menu, 0, "@string/menu_events");
        assertMenuItem(menu, 1, "@string/menu_add_location");
        assertMenuItem(menu, 2, "@string/menu_about");
    }

    @Test
    public void canSwitchToEventsView() {
        locations.onOptionsItemSelected(menu.getItem(0));
        assertNextActivity(Events.class);
    }

    @Test
    public void canLaunchNewLocationView() {
        locations.onOptionsItemSelected(menu.getItem(1));
        assertNextActivity(EditLocation.class);
    }

    @Test
    public void canLaunchNewLocationViewOnClickingTheAddButton() {
        ImageButton button = (ImageButton) locations.findViewById(R.id.createLocationButton);
        assertThat(button, is(not(nullValue())));

        button.performClick();
        assertNextActivity(EditLocation.class);
    }

    @Test
    public void canShowAboutDialog() {
        locations.onOptionsItemSelected(menu.getItem(2));
        ShadowActivity activity = shadowOf(locations);
        assertThat(activity.getNextDialog(), is(equalTo(R.id.about_proxime)));

        ShadowDialog dialog = shadowOf(locations.onCreateDialog(R.id.about_proxime, null));
        assertThat(dialog.getTitle().toString(), is(equalTo("About Proxime")));
    }

    private void assertMenuItem(TestMenu menu, int index, String title) {
        TestMenuItem testMenuItem = (TestMenuItem) menu.getItem(index);
        assertThat(testMenuItem.getTitle().toString(), is(equalTo(title)));
    }

    private void assertNextActivity(Class<? extends Activity> activityClass) {
        Intent intent = shadowOf(locations).getNextStartedActivity();
        ShadowIntent shadowIntent = shadowOf(intent);
        assertThat(shadowIntent.getComponent().getClassName(), is(equalTo(activityClass.getName())));
    }
}
