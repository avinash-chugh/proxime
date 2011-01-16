package com.proxime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
                    formattedAddress = new GeoCoderServiceUtility().getFormattedAddress(geoPoint);
                    ((TextView)(findViewById(R.id.refinedSearch))).setText(formattedAddress);
                    Intent intent =new Intent();
                    intent.putExtra("formattedAddress",formattedAddress);
                    intent.putExtra("latitude",geoPoint.getLatitudeE6()/1E6);
                    intent.putExtra("longitude",geoPoint.getLongitudeE6()/1E6);
                    setResult(EditLocation.MAP_REQUEST_CODE, intent);

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }


                return true;
        }
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        MapView mapView = setMapView();
        setOverlays(mapView);
        setUpInitialCoordinates(mapView);
        hookUpListeners();
    }

    private void setUpInitialCoordinates(MapView mapView)
    {
        double longitude =  (getIntent().getDoubleExtra("longitude", 0));
        double latitude =   (getIntent().getDoubleExtra("latitude", 0));
        String formattedAddress = (getIntent().getStringExtra("formattedAddress"));
        ((TextView)(findViewById(R.id.refinedSearch))).setText(formattedAddress);
        GeoPoint intiailLocation = new GeoPoint(((int)( latitude*1E6)),((int)( longitude*1E6)));
        mapView.getController().setCenter(intiailLocation);
        mapView.getController().setZoom(18);
    }

    private void setOverlays(MapView mapView)
    {
        List<Overlay> overlays  = mapView.getOverlays();
        overlays.clear();
        overlays.add(new MapOverlay());
    }

    private MapView setMapView()
    {
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        mapView.setStreetView(true);
        mapView.setTraffic(true);

        return mapView;
    }

    private void hookUpListeners()
    {
         ((Button)(findViewById(R.id.setLocationFromMap))).setOnClickListener(new View.OnClickListener(){

             public void onClick(View view)
             {
                 finish();
             }
         });

    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
}