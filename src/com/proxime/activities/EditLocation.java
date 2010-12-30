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
import android.widget.EditText;
import com.proxime.entities.Location;
import com.proxime.R;
import com.proxime.repositories.LocationRepository;


public class EditLocation extends Activity {
    public final static int NEW_LOCATION = 1;
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
    }

    private void setDependencies() {
        locationRepository = new LocationRepository(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MAP_REQUEST_CODE)    {
               double latitude = data.getDoubleExtra("latitude",0);
               double longitude = data.getDoubleExtra("longitude",0);

               location = new Location(getLocationName(),latitude,longitude,getSpan());
               saveAndReturnLocation();
        }
    }

    private void hookUpListeners() {
        findViewById(R.id.add_location_use_current).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useMap = false;
            }
        });
        findViewById(R.id.add_location_use_map).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                useMap = true;
            }
        });
        findViewById(R.id.add_location_save).setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (useMap) {
                    startActivityForResult(new Intent(getApplicationContext(), PickLocation.class), MAP_REQUEST_CODE);
                } else {
                    //crashes when getApplicationContext() is used for obtaining context
                    progressDialog = ProgressDialog.show(EditLocation.this, "", "Obtaining location, please wait...",true,true,null);
                    determineLocation();
                }
            }
        });
        findViewById(R.id.add_location_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveAndReturnLocation() {
        locationRepository.save(location);
        setResult(RESULT_OK, new Intent().putExtra("location", location));
        finish();
    }




    private void determineLocation() {
        if (isDebug()) {
            location = new Location(getLocationName(), 1.0, 1.0, getSpan());
            handler.sendEmptyMessage(OBTAIN_LOCATION);
            saveAndReturnLocation();
            return;
        }
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(android.location.Location loc) {
                location = new Location(getLocationName(), loc.getLatitude(), loc.getLongitude(), getSpan());
                handler.sendEmptyMessage(OBTAIN_LOCATION);
                saveAndReturnLocation();
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
        String span = ((EditText) findViewById(R.id.add_location_span)).getText().toString();
        return new Integer(span).intValue();
    }

    private boolean isDebug() {
        return ((EditText) findViewById(R.id.add_location_debug)).getText().toString().equals("yes");
    }

    private String getLocationName() {
        return ((EditText) findViewById(R.id.add_location_name)).getText().toString();
    }


}