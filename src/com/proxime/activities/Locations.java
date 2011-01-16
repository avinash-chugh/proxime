package com.proxime.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.proxime.adapters.BaseListAdapter;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.repositories.LocationRepository;

import java.util.List;

public class Locations extends Activity {
    private static final int NEW_LOCATION = R.id.add_location;
    private static final int ABOUT_DIALOG = R.id.about_proxime;
    private static final int EDIT_LOCATION = R.id.edit_location;

    private ListView locationsView;
    private LocationRepository locationRepository;
    private BaseListAdapter<Location> adapter;

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
        adapter = new BaseListAdapter<Location>(this, locations);
        locationsView.setAdapter(adapter);
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
                long id = ((BaseListAdapter.ViewHolder) view.getTag()).id;
                setResult(RESULT_OK, new Intent().putExtra("location", locationRepository.load(id)));
                finish();
            }
        });

        registerForContextMenu(locationsView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case NEW_LOCATION: {
                Location location = getResult(data);
                showStatus(R.string.toast_location_created, location);
                adapter.add(location);
                break;
            }
            case EDIT_LOCATION: {
                Location location = getResult(data);
                showStatus(R.string.toast_location_modified, location);
                loadLocations();
                break;
            }
        }
    }

    private Location getResult(Intent data) {
        return (Location) data.getExtras().get("location");
    }

    private void showStatus(int message, Location location) {
        Toast.makeText(this, getString(message, location.getName()), Toast.LENGTH_SHORT).show();
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.locations_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Location location = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.edit_location: {
                editLocation(location.getId());
                break;
            }
            case R.id.delete_location: {
                deleteLocation(location);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void deleteLocation(Location location) {
        locationRepository.delete(location.getId());
        adapter.remove(location);
        showStatus(R.string.toast_location_removed, location);
    }

    private void editLocation(long id) {
        Intent intent = new Intent(getApplicationContext(), EditLocation.class).putExtra("location_id", id);
        startActivityForResult(intent, EDIT_LOCATION);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case ABOUT_DIALOG:
                return new AboutDialog(this).show();
        }
        return super.onCreateDialog(id, args);
    }

    private void newLocation() {
        Intent intent = new Intent(this, EditLocation.class);
        startActivityForResult(intent, NEW_LOCATION);
    }
}