package com.proxime;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import org.json.JSONException;

import java.util.List;

public class LocationFromMap extends MapActivity
{
    public class MapOverlay extends com.google.android.maps.Overlay
    {
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView)
        {
            String formattedAddress ="";
            if (motionEvent.getAction()==1)
            {
                GeoPoint point = mapView.getProjection().fromPixels(
                        (int)motionEvent.getX(),(int)motionEvent.getY());

                try
                {
                    formattedAddress = new GeoCoderServiceUtility(point).getFormattedAddress();
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Toast.makeText(getBaseContext(), formattedAddress, Toast.LENGTH_SHORT).show();
                return true;
            }

        return false;
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