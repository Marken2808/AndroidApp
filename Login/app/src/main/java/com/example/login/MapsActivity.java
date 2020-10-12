package com.example.login;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST = 500;
    private Button sOut;
    private SearchView sView;
    private Spinner spinType;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fLocProCli;
    private Location curLoc;
    private static final int REQUEST_CODE = 101;

    ArrayList<LatLng> listCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        listCoords = new ArrayList<>();
        signOutAction();
        searchViewAction();

        fLocProCli = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();




    }

    private void fetchLastLocation() {

        @SuppressLint("MissingPermission")
        Task<Location> task = fLocProCli.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    curLoc = location;
                    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);

                    String url = tsPlaceAPI(curLoc.getLatitude(),curLoc.getLongitude());
                    new TransportPlaceTask().execute(url);
                }
            }
        });

    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (listCoords.size() == 1){
                    listCoords.clear();
                    mMap.clear();
                }

                listCoords.add(latLng);
                //
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.addMarker(markerOptions);

                //showToast("new here: " + curLoc.getLatitude() +","+ curLoc.getLongitude());
                String url = tsPlaceAPI(latLng.latitude,latLng.longitude);
                new TransportPlaceTask().execute(url);
            }
        });



        LatLng latLng = new LatLng(curLoc.getLatitude(),curLoc.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,14));
        mMap.addMarker(markerOptions);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
        }
    }

    public String ggPlaceAPI (double lat, double lon) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"+
                "location=" + lat +","+ lon +
                "&radius=2000" +
                "&types=bus_station" +
                "&name=bus" +
                "&key=AIzaSyAr5vgNYFcS-FptDZrzd18JVK3xjXehK_o";

        return url;
    }

    public void searchViewAction (){

        sView = findViewById(R.id.searchView);
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                double lat = listCoords.get(0).latitude;
//                double lon = listCoords.get(0).longitude;
//
//                showToast("new here: " + curLoc.getLatitude() +","+ curLoc.getLongitude());
//                String url = ggPlaceAPI(lat,lon);
//                new PlaceTask().execute(url);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void signOutAction (){
        //sign out
        sOut = findViewById(R.id.signOut);
        sOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MapsActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    public void showToast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public String tsPlaceAPI (double lat, double lon) {

        // https://transportapi.com/v3/uk/places.json?app_id=9281db1a&app_key=86dda2ae6c899b579364035ec136f7b0&lat=51.5045861&lon=-2.5660455&type=bus_stop
        showToast("called");
        String appID = "app_id=9281db1a";
        String appKey = "app_key=86dda2ae6c899b579364035ec136f7b0";
        String type = "type=bus_stop";
        String param = appID + "&" + appKey +"&lat=" + lat + "&lon" + lon + "&" + type;
        String url = "https://transportapi.com/v3/uk/places.json?app_id=9281db1a&app_key=86dda2ae6c899b579364035ec136f7b0&lat=" + lat + "&lon=" + lon +"&type=bus_stop";

        return url;
    }


    private String downloadURL(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(stream);

        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder builder = new StringBuilder();
        String line = "";

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();
        inputStreamReader.close();
        return data;
    }

    public class GooglePlaceTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadURL(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new GoogleParseTask().execute(s);
        }
    }

    public class GoogleParseTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            GooglePlacesParser jsonParser = new GooglePlacesParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;

            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();
            for (int i = 0; i < hashMaps.size(); i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lon = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat,lon);

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                mMap.addMarker(options);


            }
        }
    }


    public class TransportPlaceTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadURL(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new TransportParseTask().execute(s);
        }
    }

    public class TransportParseTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            TransportPlaceParser jsonParser = new TransportPlaceParser();
            List<HashMap<String, String>> mapList = null;
            JSONObject object = null;

            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();
            for (int i = 0; i < hashMaps.size(); i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("latitude"));
                double lon = Double.parseDouble(hashMapList.get("longitude"));
                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat,lon);

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                mMap.addMarker(options);


            }
        }
    }
}