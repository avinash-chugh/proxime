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
    private LocationManager manager;
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
        setDefaultLocation();
        loadLocation();
    }

    private void setDefaultLocation()   {
        location = new Location("",1.0,1.0,0);
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
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MAP_REQUEST_CODE) {
            double latitude = data.getDoubleExtra("latitude", 0);
            double longitude = data.getDoubleExtra("longitude", 0);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            ((TextView) findViewById(R.id.formattedAddress)).setText(data.getStringExtra("formattedAddress"));
        }
    }

    private void hookUpListeners() {

        findViewById(R.id.addLocation).setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                //String geoUriString = getResources().getString(R.string.map_location);
//                Uri geoUri = Uri.parse("geo:0,180?z=1");
//                Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
//                startActivityForResult(mapCall,MAP_REQUEST_CODE);

                startActivityForResult(new Intent(getApplicationContext(), PickLocation.class), MAP_REQUEST_CODE);
            }
        });
        findViewById(R.id.save_location).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveLocation();
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
        String provider = isGPSEnabled() ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
        manager.requestLocationUpdates(provider, 0, 0, locationListener);
    }

    private int getSpan() {
        return new Integer(getViewText(R.id.edit_location_span));
    }

    private boolean isDebug() {
        return false;
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

    public boolean isGPSEnabled() {
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}