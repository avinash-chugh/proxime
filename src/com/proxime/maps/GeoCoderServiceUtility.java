package com.proxime.maps;

import com.google.android.maps.GeoPoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GeoCoderServiceUtility
{
    private static String webServiceUrl = "http://maps.googleapis.com/maps/api/geocode";

    public GeoCoderServiceUtility()
    {
    }

    public String getFormattedAddress(GeoPoint point) throws JSONException
    {
       return getGeoCoderResponse(point).getFormattedAddress();
    }

    public ArrayList<String> getFormattedAddress(String address) throws JSONException
    {
        ArrayList<String> toReturn = new ArrayList<String>();

        String urlToConnect = getQueryStringForResquest(address);
        JSONObject jsonResponse = RestJsonClient.connect(urlToConnect);

        JSONArray results = jsonResponse.getJSONArray("results");
        for (int i= 0;i<results.length();i++)   {
            JSONObject result = (JSONObject) results.get(i);
            String formattedAddress = result.getString("formatted_address");
            toReturn.add(formattedAddress);
        }


        return toReturn;
    }


    public String getQueryStringForResquest(String formattedAddress)    {
       return  webServiceUrl+"/json?address=" + removeSpaces(formattedAddress) + "&sensor=true";
    }

    private String removeSpaces(String formattedAddress)
    {
        return formattedAddress.replace(' ','+');
    }

    public GeoCoderResponse getGeoCoderResponse(GeoPoint point)
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




