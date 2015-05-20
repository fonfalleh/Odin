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

public class MapsActivity extends FragmentActivity implements RequestInterface {

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

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //Start Request Thread
        //The process(String str) function will be executed when the request is finished
        float zoom = 12;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
        new RequestManager(this).execute("", "GET");
        mMap.addMarker(new MarkerOptions().position(target).title("YOU ARE HERE!")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        //Current location has azure color
    }

    /**
     * When (if) the server returns a result, it is given in the form of the string str.
     * The string is interpreted as a JSONArray and it's fields are extrated into locations that can
     * be added to the map.
     *
     * @param str The string returned from the server.
     */
    @Override
    public void process(String str) {
        try {
            JSONArray incidents = new JSONArray(str);

            for (int i = 0; i < incidents.length(); i++) {
                double lat = incidents.getJSONObject(i).getDouble("lat");
                double lng = incidents.getJSONObject(i).getDouble("lng");
                String incident = incidents.getJSONObject(i).getString("incident");
                String date = incidents.getJSONObject(i).getString("created_at");
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .title(date) //TODO Add actual time.
                        .snippet(incident)); //Concatenates after 42 chars.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}