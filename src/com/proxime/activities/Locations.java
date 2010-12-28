package com.proxime.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.proxime.adapters.CustomListAdapter;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.repositories.LocationRepository;

import java.util.List;

public class Locations extends Activity {
    private static final int NEW_LOCATION = 1;
    private static final int ABOUT_DIALOG = 1;
    private ListView locationsView;
    private LocationRepository locationRepository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_list);
        setDependencies();
        loadLocations();
        hookUpEvents();
    }

    private void setDependencies() {
        locationRepository = new LocationRepository(this);
    }

    private void loadLocations() {
        List<Location> locations = locationRepository.loadAll();
        locationsView = (ListView) findViewById(R.id.locationsList);
        locationsView.setTextFilterEnabled(true);
        locationsView.setAdapter(new CustomListAdapter<Location>(this,locations));
    }

    private void hookUpEvents() {
        Button createButton = (Button) findViewById(R.id.createLocationButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newLocation();
            }
        });

        locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long id = ((CustomListAdapter.ViewHolder) view.getTag()).id;
                setResult(RESULT_OK, new Intent().putExtra("location", locationRepository.load(id)));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case NEW_LOCATION :
                Location location = (Location) data.getExtras().get("location");
                CustomListAdapter<Location> adapter = (CustomListAdapter<Location>) locationsView.getAdapter();
                adapter.add(location);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.locations_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_location: {
                newLocation();
                break;
            }
            case R.id.view_events: {
                Intent intent = new Intent(this, Events.class);
                startActivity(intent);
                break;
            }
            case R.id.about_proxime: {
                showDialog(ABOUT_DIALOG);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case ABOUT_DIALOG:
                return new AboutDialog(this).Show();
        }
        return super.onCreateDialog(id, args);
    }

    private void newLocation() {
        Intent intent = new Intent(this, EditLocation.class);
        startActivityForResult(intent, NEW_LOCATION);
    }
}