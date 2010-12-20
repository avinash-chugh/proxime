package com.proxime;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoCoderResponse
{
    private JSONObject jsonResponse;

    public GeoCoderResponse(JSONObject jsonResponse)
    {
        this.jsonResponse = jsonResponse;
    }

    public String getFormattedAddress() throws JSONException
    {
        return jsonResponse.getJSONArray("results").getJSONObject(0).getString("formatted_address");
    }
}
