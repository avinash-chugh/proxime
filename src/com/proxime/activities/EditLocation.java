package com.proxime.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.repositories.LocationRepository;

public class EditLocation extends Activity {
    private boolean useMap;
    private Location location;
    private LocationRepository locationRepository;

    private ProgressDialog progressDialog;
    private int OBTAIN_LOCATION = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
        }
    };
    public static final int MAP_REQUEST_CODE = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_edit);
        hookUpListeners();
        setDependencies();
        loadLocation();
    }

    private void loadLocation() {
        long id = getIntent().getLongExtra("location_id", -1);
        if (id < 0) return;

        location = locationRepository.load(id);

        setTitle(location.getName());
        setText(R.id.edit_location_name, location.getName());
        if (location.getSpan() > 0) setText(R.id.edit_location_span, Integer.toString(location.getSpan()));
    }

    private void setDependencies() {
        locationRepository = new LocationRepository(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_CODE) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);

            location.setLatitude(latitude);
            location.setLongitude(longitude);
            saveLocation();
        }
    }

    private void hookUpListeners() {
        findViewById(R.id.edit_location_use_current).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useMap = false;
            }
        });
        findViewById(R.id.edit_location_use_map).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useMap = true;
            }
        });
        findViewById(R.id.save_location).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (useMap) {
                    startActivityForResult(new Intent(getApplicationContext(), PickLocation.class), MAP_REQUEST_CODE);
                } else {
                    //crashes when getApplicationContext() is used for obtaining context
                    progressDialog = ProgressDialog.show(EditLocation.this, "", "Obtaining location, please wait...", true, true, null);
                    determineLocation();
                }
            }
        });
        findViewById(R.id.cancel_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveLocation() {
        location.setName(getLocationName());
        location.setSpan(getSpan());
        
        locationRepository.save(location);
        setResult(RESULT_OK, new Intent().putExtra("location", location));
        finish();
    }

    private void determineLocation() {
        if (isDebug()) {
            location.setLatitude(1.0);
            location.setLongitude(1.0);

            handler.sendEmptyMessage(OBTAIN_LOCATION);
            saveLocation();
            return;
        }
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location mapPlace) {
                location.setLatitude(mapPlace.getLatitude());
                location.setLongitude(mapPlace.getLongitude());
                handler.sendEmptyMessage(OBTAIN_LOCATION);
                saveLocation();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {
            }
        };
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, locationListener);
    }

    private int getSpan() {
        return new Integer(getViewText(R.id.edit_location_span));
    }

    private boolean isDebug() {
        return "yes".equals(getViewText(R.id.edit_location_debug));
    }

    private String getLocationName() {
        return getViewText(R.id.edit_location_name);
    }

    public String getViewText(int resourceId) {
        return ((TextView) findViewById(resourceId)).getText().toString();
    }

    private void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }
}