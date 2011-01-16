package com.proxime.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
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
            ((TextView)(findViewById(R.id.userSelectedLocation))).setText((data.getStringExtra("formattedAddress")));
            ((ArrayAdapter<GeoCoderResponse>)((ListView)(findViewById(R.id.searchResultsList))).getAdapter()).clear();
            ((EditText)(findViewById(R.id.searchTextBoxForLocation))).setText("");
        }
    }

    private void hookUpListeners() {
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
                GeoCoderResponse item = (GeoCoderResponse) adapterView.getAdapter().getItem(i);
                try
                {
                    ((TextView)(findViewById(R.id.userSelectedLocation))).setText(item.getFormattedAddress());
                    location.setLatitude(new Double(item.getLatitude()));
                    location.setLongitude(new Double(item.getLongitude()));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.gotoMap).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if (((TextView)findViewById(R.id.userSelectedLocation)).getText().equals("")) return;
                Intent newIntent = new Intent(getApplicationContext(), PickLocation.class);
                newIntent.putExtra("latitude", location.getLatitude());
                newIntent.putExtra("longitude", location.getLongitude());
                newIntent.putExtra("formattedAddress",((TextView)findViewById(R.id.userSelectedLocation)).getText());
                startActivityForResult(newIntent, MAP_REQUEST_CODE);
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


    public String getSearchString()
    {
        return ((TextView)findViewById(R.id.searchTextBoxForLocation)).getText().toString();
    }
}