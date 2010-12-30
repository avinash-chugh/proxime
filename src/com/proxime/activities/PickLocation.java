package com.proxime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.proxime.R;
import com.proxime.maps.GeoCoderServiceUtility;
import org.json.JSONException;

import java.util.List;

public class PickLocation extends MapActivity
{
    public class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean onTap(GeoPoint geoPoint, MapView mapView)
        {
            String formattedAddress ="";
                try
                {
                    formattedAddress = new GeoCoderServiceUtility(geoPoint).getFormattedAddress();
                    Intent intent =new Intent();
                    intent.putExtra("location",formattedAddress);
                    intent.putExtra("latitude",geoPoint.getLatitudeE6()/1E6);
                    intent.putExtra("longitude",geoPoint.getLongitudeE6()/1E6);
                    setResult(EditLocation.MAP_REQUEST_CODE, intent);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Toast.makeText(getBaseContext(), formattedAddress, Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        List<Overlay> overlays  = mapView.getOverlays();
        overlays.clear();
        overlays.add(new MapOverlay());
    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
}