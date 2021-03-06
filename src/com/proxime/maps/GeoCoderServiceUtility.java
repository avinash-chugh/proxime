package com.proxime.maps;

import com.google.android.maps.GeoPoint;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GeoCoderServiceUtility {
    private static String webServiceUrl = "http://maps.googleapis.com/maps/api/geocode";

    public String getFormattedAddress(GeoPoint point) throws JSONException {
        return getGeoCoderResponse(point).getFormattedAddress();
    }

    public List<GeoCoderResponse> getFormattedAddresses(String address) throws JSONException {
        List<GeoCoderResponse> responseObjects = new ArrayList<GeoCoderResponse>();

        String urlToConnect = getQueryStringForRequest(address);
        JSONObject jsonResponse = RestJsonClient.connect(urlToConnect);

        JSONArray resultObjects = jsonResponse.getJSONArray("results");

        for (int i = 0; i < resultObjects.length(); i++) {
            responseObjects.add(new GeoCoderResponse((JSONObject) resultObjects.get(i)));
        }

        return responseObjects;
    }

    public String getQueryStringForRequest(String formattedAddress) {
        return String.format(webServiceUrl + "/json?address=%s&sensor=true", removeSpaces(formattedAddress));
    }

    private String removeSpaces(String formattedAddress) {
        return formattedAddress.replace(' ', '+');
    }

    public GeoCoderResponse getGeoCoderResponse(GeoPoint point) throws JSONException
    {
        String urlToConnect = webServiceUrl + getQueryStringForRequest(point);
        JSONObject jsonResponse = RestJsonClient.connect(urlToConnect);
        JSONArray resultObjects = jsonResponse.getJSONArray("results");
        return new GeoCoderResponse(resultObjects.getJSONObject(0));
    }

    private String getQueryStringForRequest(GeoPoint point) {
        double latitude = point.getLatitudeE6() / 1E6;
        double longitude = point.getLongitudeE6() / 1E6;
        return String.format("/json?latlng=%f,%f&sensor=true", latitude, longitude);
    }
}




