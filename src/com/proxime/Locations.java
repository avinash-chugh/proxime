package com.proxime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.proxime.repositories.LocationRepository;

import java.util.List;

public class Locations extends Activity {
    private ListView locationsView;
    private LocationRepository locationRepository;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locations);
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
                startActivityForResult(new Intent(getApplicationContext(), AddLocation.class), AddLocation.NEW_LOCATION);
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
            case AddLocation.NEW_LOCATION :
                Location location = (Location) data.getExtras().get("location");
                CustomListAdapter<Location> adapter = (CustomListAdapter<Location>) locationsView.getAdapter();
                adapter.add(location);
                break;
        }
    }
}