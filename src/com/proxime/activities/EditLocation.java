package com.proxime.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.proxime.R;
import com.proxime.entities.Location;
import com.proxime.maps.GeoCoderResponse;
import com.proxime.maps.GeoCoderServiceUtility;
import com.proxime.repositories.LocationRepository;
import org.json.JSONException;

import java.util.Date;
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
                try
                {
                List<GeoCoderResponse> possibleAddresses = new GeoCoderServiceUtility().getFormattedAddresses(getSearchString());
                ListView eventsView = (ListView) findViewById(R.id.searchResultsList);
                eventsView.setTextFilterEnabled(true);
                ArrayAdapter<GeoCoderResponse> adapter = new ArrayAdapter<GeoCoderResponse>(getBaseContext(),android.R.layout.simple_list_item_1, possibleAddresses);
                eventsView.setAdapter(adapter);
                } catch (JSONException e)
                    {
                    e.printStackTrace();
                    }
            }
        });

        ((ListView) findViewById(R.id.searchResultsList)).setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                //long id = ((EventListAdapter.EventViewHolder) view.getTag()).id;
                //Temporary. Will be removed.<vineethv>
                GeoCoderResponse item = (GeoCoderResponse) adapterView.getAdapter().getItem(i);
                try
                {
                    ((TextView)(findViewById(R.id.userSelectedLocation))).setText(item.getFormattedAddress());
                    location.setLatitude(new Double(item.getLatitude())/1E6);
                    location.setLongitude(new Double(item.getLongitude())/1E6);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                //startActivity(new Intent(getApplicationContext(), ViewEvent.class).putExtra("event_id", id));
            }
        });
    }

    private void saveLocation() {
        location.setName(new Date().toString());
        location.setSpan(25);

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

    public String getSearchString()
    {
        return ((TextView)findViewById(R.id.searchTextBoxForLocation)).getText().toString();
    }
}