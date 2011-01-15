package com.proxime.maps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GeoCoderResponse
{
    private JSONObject jsonResponse;

    public GeoCoderResponse(JSONObject jsonResponse)
    {
        this.jsonResponse = jsonResponse;
    }

    @Deprecated
    public String oldGetFormattedAddress() throws JSONException
    {
        return jsonResponse.getJSONArray("results").getJSONObject(0).getString("formatted_address");
    }

    public String getFormattedAddress() throws JSONException{
       return jsonResponse.getString("formatted_address").toString();
    }

    public String getLatitude() throws JSONException    {
       return jsonResponse.getJSONObject("geometry").getJSONObject("location").getString("lat").toString();
    }

    public String getLongitude() throws JSONException   {
        return jsonResponse.getJSONObject("geometry").getJSONObject("location").getString("lng").toString();
    }

    @Override
    public String toString()    {
        try
        {
            return getFormattedAddress();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    @Deprecated
    public List<String> oldGetFormattedAddresses()  throws JSONException
    {
       List<String> toReturn = new ArrayList<String>();
        JSONArray results = jsonResponse.getJSONArray("results");
        for (int i= 0;i<results.length();i++)   {
            JSONObject result = (JSONObject) results.get(i);
            String formattedAddress = result.getString("formatted_address");
            toReturn.add(formattedAddress);
        }
        return toReturn;
    }
}
