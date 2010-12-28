package com.proxime.maps;

import com.google.android.maps.GeoPoint;
import org.json.JSONException;
import org.json.JSONObject;


public class GeoCoderServiceUtility
{
    private static String webServiceUrl = "http://maps.googleapis.com/maps/api/geocode";
    private GeoPoint point;

    public GeoCoderServiceUtility(GeoPoint point)
    {
        this.point = point;
    }

    public String getFormattedAddress() throws JSONException
    {
       return getGeoCoderResponse().getFormattedAddress();
    }

    public GeoCoderResponse getGeoCoderResponse()
    {
        String urlToConnect = webServiceUrl+getQueryStringForRequest(point);
        JSONObject jsonResponse = RestJsonClient.connect(urlToConnect);
        return new GeoCoderResponse(jsonResponse);
    }

    private String getQueryStringForRequest(GeoPoint point)
    {
        double latitude = point.getLatitudeE6() / 1E6;
        double longitude = point.getLongitudeE6() / 1E6;
        return "/json?latlng="+ latitude +","+ longitude +"&sensor=true";
    }


}




