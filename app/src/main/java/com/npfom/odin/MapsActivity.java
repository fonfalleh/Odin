package com.npfom.odin;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

/*
    Activity for displaying a map with all reported incidents as markers.
 */
public class MapsActivity extends FragmentActivity implements RequestInterface {

    // Map components.
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng target;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    // Generated method for setting up map (if needed).
    private void setUpMapIfNeeded() {

        // If we can get location from the Location manager (Device GPS), update target.
        // Otherwise, use default location
        Location tmpLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (tmpLocation != null) {
            target = new LatLng(tmpLocation.getLatitude(),tmpLocation.getLongitude());
        } else {
            target = new LatLng(57.708870, 11.974560);
        }

        float zoom = 13;
        float bearing = 113;
        float tilt = 0;
        GoogleMapOptions opt = new GoogleMapOptions();
        opt.camera(new CameraPosition(target, zoom, tilt, bearing));
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    // This is where we can add markers or lines, add listeners or move the camera.
    private void setUpMap() {

        float zoom = 12;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
        // Current location marker has azure color
        mMap.addMarker(new MarkerOptions().position(target).title("YOU ARE HERE!")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //Start Request Thread
        //The process(String str) function will be executed when the request is finished
        new RequestManager(this).execute("", "GET");

    }

    // When (if) the server returns a result, it is given in the form of the string str.
    // The string is interpreted as a JSONArray and it's fields are extracted into locations that can
    // be added to the map.
    @Override
    public void process(String str) {
        try {
            // String is turned into a JSONArray.
            JSONArray incidents = new JSONArray(str);
            // For each JSONObject, take its data and use it to produce info for a marker.
            for (int i = 0; i < incidents.length(); i++) {
                double lat = incidents.getJSONObject(i).getDouble("lat");
                double lng = incidents.getJSONObject(i).getDouble("lng");
                String incident = incidents.getJSONObject(i).getString("incident");
                String date = incidents.getJSONObject(i).getString("created_at");
                // Place the marker on the map.
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))     // Sets coordinates
                        .title(date)                        // Sets timestamp as title
                        .snippet(incident));                // Sets incident description as subtext
                        // Observation: Concatenates after 42 chars.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}