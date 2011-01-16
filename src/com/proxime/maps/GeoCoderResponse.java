package com.proxime.maps;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoCoderResponse
{
    private JSONObject jsonResponse;

    public GeoCoderResponse(JSONObject jsonResponse)
    {
        this.jsonResponse = jsonResponse;
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
}
