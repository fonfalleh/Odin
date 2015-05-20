package com.npfom.odin;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapMarkerActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker marker;
    private double lat, lng;

    /**
     * Initialization method. Takes coordinates from parent activity and uses them as marker coordinates.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
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
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setOnMarkerDragListener(this);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //Starts map somewhat zoomed in over user location.
        float zoom = 12;
        LatLng target = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
        marker = mMap.addMarker(new MarkerOptions()
                .position(target)
                .title("Incident"));
        marker.setDraggable(true);
    }

    /**
     * When the user is done choosing a location, the coordinates are sent back to the Main Activity.
     * @param view
     */
    public void setCoords(View view) {
        Intent intent = new Intent();
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    /**
     * When the user moves a marker, the coordinates to be sent back are updated with the position
     * of the marker.
     * @param marker The marker that has been moved.
     */
    @Override
    public void onMarkerDragEnd (Marker marker){
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
        Log.d("MapMarkerActivity", "lat: "+lat+" lng: "+lng);
    }
}
