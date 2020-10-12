package com.example.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GooglePlacesParser {
    private HashMap<String, String> parseJsonObject (JSONObject object) throws JSONException {
        HashMap<String, String> dataList = new HashMap<>();
        String name = object.getString("name");
        String lat = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
        String lon = object.getJSONObject("geometry").getJSONObject("location").getString("lng");

        dataList.put("name", name);
        dataList.put("lat" , lat);
        dataList.put("lng" , lon);

        return dataList;
    }

    private List<HashMap<String,String>> parseJsonArray (JSONArray jsonArray) throws JSONException {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
            dataList.add(data);
        }
        return dataList;
    }

    public List<HashMap<String,String>> parseResult (JSONObject object) throws JSONException {
        JSONArray jsonArray = null;
        jsonArray = object.getJSONArray("results");
        return parseJsonArray(jsonArray);
    }

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.4586083,-2.5864018&radius=1500&type=bus_station&key=AIzaSyAr5vgNYFcS-FptDZrzd18JVK3xjXehK_o
}
