package com.npfom.odin;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/*
    Activity for selecting custom coordinates to report.
 */
public class MapMarkerActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener{

    // Map components.
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker marker;
    private double lat, lng;


    // Initialization method. Takes coordinates from parent activity and uses them as marker coordinates.
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

    // Generated method for setting up map (if needed).
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

    // This is where we can add markers or lines, add listeners or move the camera.
    // This should only be called once and when we are sure that mMap is not null.
    private void setUpMap() {
        //Starts map somewhat zoomed in over user location.
        float zoom = 12;
        LatLng target = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));

        // Adds the marker ( using coordinates from MainActivty).
        marker = mMap.addMarker(new MarkerOptions()
                .position(target)
                .title("Incident"));
        marker.setDraggable(true);
    }


    // When the user is done choosing a location, the coordinates are sent back to the Main Activity.
    public void setCoords(View view) {
        Intent intent = new Intent();
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    // Inherited methods needed, currently not used.
    @Override
    public void onMarkerDragStart(Marker marker) {

    }
    @Override
    public void onMarkerDrag(Marker marker) {

    }


    // When the user moves a marker, the coordinates to be sent back are updated with the position of the marker.
    @Override
    public void onMarkerDragEnd (Marker marker){
        lat = marker.getPosition().latitude;
        lng = marker.getPosition().longitude;
        Log.d("MapMarkerActivity", "lat: "+lat+" lng: "+lng);
    }
}
