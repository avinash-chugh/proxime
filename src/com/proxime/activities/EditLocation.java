package com.proxime.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.maps.GeoCoderServiceUtility;
import com.proxime.repositories.LocationRepository;
import org.json.JSONException;

import java.util.List;

public class EditLocation extends Activity
{
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

        try
        {
            List<String> possibleAddresses = new GeoCoderServiceUtility().getFormattedAddress("Indiranagar");
            ListView eventsView = (ListView) findViewById(R.id.searchResultsList);
            eventsView.setTextFilterEnabled(true);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, possibleAddresses);
            eventsView.setAdapter(adapter);
        } catch (JSONException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void setDefaultLocation()   {
        location = new Location("",1.0,1.0,0);
    }

    private void loadLocation() {
        long id = getIntent().getLongExtra("location_id", -1);
        if (id < 0) return;

        location = locationRepository.load(id);

        setTitle(location.getName());

        //if (location.getSpan() > 0) setText(R.id.edit_location_span, Integer.toString(location.getSpan()));
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
            //((TextView) findViewById(R.id.formattedAddress)).setText(data.getStringExtra("formattedAddress"));
        }
    }

    private void hookUpListeners() {

//        findViewById(R.id.addLocation).setOnClickListener(new View.OnClickListener(){
//            public void onClick(View view)
//            {
//                startActivityForResult(new Intent(getApplicationContext(), PickLocation.class), MAP_REQUEST_CODE);
//            }
//        });
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
        findViewById(R.id.searchLocationButton).setOnClickListener(new View.OnClickListener()   {
            public void onClick(View view)
            {




            }
        });
    }

    private void saveLocation() {
//        location.setName(getLocationName());
//        location.setSpan(getSpan());

        locationRepository.save(location);
        setResult(RESULT_OK, new Intent().putExtra("location", location));
        finish();
    }

//    private int getSpan() {
//        return new Integer(getViewText(R.id.edit_location_span));
//    }
//
//    private String getLocationName() {
//        return getViewText(R.id.edit_location_name);
//    }



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