package com.proxime.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        hookUpListeners();
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
        return mapView;
    }

    private void hookUpListeners()
    {
        View.OnClickListener onClickListener = new View.OnClickListener()
        {

            public void onClick(View view)
            {
                final CharSequence[] items = {"Forum", "Green", "Blue"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Pick a color");
                builder.setItems(items, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int item)
                    {
                        Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };
        findViewById(R.id.searchLocationOnMap).setOnClickListener(onClickListener);

    }

    @Override
    protected boolean isRouteDisplayed()
    {
        return false;
    }
}