package com.example.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransportPlaceParser {
    private HashMap<String, String> parseJsonObject (JSONObject object) throws JSONException {
        HashMap<String, String> dataList = new HashMap<>();
        String name = object.getString("name");
        String lat = object.getString("latitude");
        String lon = object.getString("longitude");

        dataList.put("name", name);
        dataList.put("latitude" , lat);
        dataList.put("longitude" , lon);

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
        jsonArray = object.getJSONArray("member");
        return parseJsonArray(jsonArray);
    }

    //https://transportapi.com/v3/uk/places.json?app_id=9281db1a&app_key=86dda2ae6c899b579364035ec136f7b0&lat=51.5045861&lon=-2.5660455&type=bus_stop
}
