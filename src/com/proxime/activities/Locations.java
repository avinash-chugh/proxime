package com.proxime.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.proxime.adapters.BaseListAdapter;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.repositories.LocationRepository;

import java.util.List;

public class Locations extends Activity {
    private static final int ABOUT_DIALOG = R.id.about_proxime;
    private static final int EDIT_LOCATION = R.id.edit_location;
    public static final int SET_LOCATION_DIALOG = R.id.add_location;

    private ListView locationsView;
    private LocationRepository locationRepository;
    private BaseListAdapter<Location> adapter;
    private Location location;
    private EditText searchView;

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
                showLocationDialog();
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

        searchView = (EditText) findViewById(R.id.search_location);
        searchView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int count, int after) {
                adapter.filter(s.toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
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
                showLocationDialog();
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
        location = adapter.getItem(info.position);

        switch (item.getItemId()) {
            case R.id.edit_location: {
                editLocation(location.getId());
                break;
            }
            case R.id.rename_location: {
                showDialog(SET_LOCATION_DIALOG);
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


    private void showLocationDialog() {
        location = null;
        showDialog(SET_LOCATION_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case SET_LOCATION_DIALOG: {
                LocationDialog locationDialog = new LocationDialog(this);
                locationDialog.setLocationChangeListener(new LocationChangeListener() {
                    public void change(Location location, boolean isNew) {
                        if (isNew) {
                            showStatus(R.string.toast_location_created, location);
                            adapter.add(location);
                        }
                        else {
                            showStatus(R.string.toast_location_modified, location);
                            loadLocations();
                        }
                    }
                });
                return locationDialog.show(location);
            }
            case ABOUT_DIALOG:
                return new AboutDialog(this).show();
        }
        return super.onCreateDialog(id, args);
    }
}