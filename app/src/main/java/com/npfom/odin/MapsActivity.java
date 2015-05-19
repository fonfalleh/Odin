package com.npfom.odin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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

import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements RequestInterface{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 0);
        lng = intent.getDoubleExtra("lng", 0);
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
        float zoom = 13;
        LatLng target = new LatLng(57.708870,11.974560);
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
        LatLng target = new LatLng(57.708870,11.974560);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target,zoom));
        new RequestManager(this).execute("", "GET");
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("YOU!")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        //Current location has azure color

    }

    /**
     * When given a list of locations (from database or other source), the locations will be marked on the map.
     * @param locations Places to be marked on the map.
     */
    public void addMarkers(List<LatLng> locations){
        for(LatLng l : locations){
            if(l != null) {
                mMap.addMarker(new MarkerOptions().position(l).title("Marker"));
            }
        }
    }

    /**
     * When (if) the server returns a result, it is given in the form of the string str.
     * The string is interpreted as a JSONArray and it's fields are extrated into locations that can
     * be added to the map.
     * @param str The string returned from the server.
     */
    @Override
    public void process(String str) {
        try{
            JSONArray incidents = new JSONArray(str);
            LinkedList<LatLng> coords = new LinkedList<LatLng>();

            for(int i = 0; i < incidents.length(); i++ ){
                double lat = incidents.getJSONObject(i).getDouble("lat");
                double lng = incidents.getJSONObject(i).getDouble("lng");
                Log.d("MapsActivity","lat: "+lat+" long: "+lng );
                coords.add(new LatLng(lat, lng));
            }
            addMarkers(coords);
        } catch (JSONException e) { e.printStackTrace();}

    }
}
