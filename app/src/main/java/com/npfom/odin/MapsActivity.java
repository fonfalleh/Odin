package com.npfom.odin;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements RequestInterface{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
        opt.camera(new CameraPosition(target,zoom,tilt,bearing));
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
        // Get the location manager
        //TODO Debugging purposes, refactor later
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat, lng;
        if(location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            Log.d("MapsActivity", "GPS fail, using phony coords.");
            lat = 57.687163; // TODO test 57.687163, 11.949335 (Folkdansringen Göteborg i Slottskogen)
            lng = 11.949335;
        }
        checkAndAddMarkers();
        //Add gps-tests
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker"));
        /* new RequestManager(this).execute("", "GET");
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker")); */
        /* mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("YOU!")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))); //Now has color */
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
    public void checkAndAddMarkers(){
        /**
         * Keys in DB
         * id
         * incident
         * lat
         * lng
         */
        //TODO make it happen :D

    }

    //In this case the process function will create a json
    @Override
    public void process(String str) {

        try{
            //TODO debugging
            String sak = "[{\"id\": 2,\"incident\": \"RESTtest\",\"lat\": 6.000000,\"lng\": 6.000000,\"created_at\": \"2015-04-27 20:16:48\"}, {\"id\": 3,\"incident\": \"RestRest\",\"lat\": 1.001000,\"lng\": 1.001000,\"created_at\": \"2015-04-29 04:19:46\"}, {\"id\": 4,\"incident\": \"test from java\",\"lat\": 3.001000,\"lng\": 4.002000,\"created_at\": \"2015-04-29 04:20:42\"}, {\"id\": 5,\"incident\": \"test from java\",\"lat\": 3.001000,\"lng\": 4.002000,\"created_at\": \"2015-04-29 04:37:57\"}, {\"id\": 6,\"incident\": \"pplsssss\",\"lat\": 1.001000,\"lng\": 3.002000,\"created_at\": \"2015-04-29 05:14:08\"}, {\"id\": 7,\"incident\": \":-(\",\"lat\": 57.692783,\"lng\": 11.975537,\"created_at\": \"2015-04-29 05:25:51\"}, {\"id\": 8,\"incident\": \"hjäääälp\",\"lat\": 57.692741,\"lng\": 11.975511,\"created_at\": \"2015-04-29 05:34:55\"}, {\"id\": 9,\"incident\": \"det regnar :-(\",\"lat\": 57.692436,\"lng\": 11.975169,\"created_at\": \"2015-04-29 05:58:17\"}, {\"id\": 10,\"incident\": \"#kränkt\",\"lat\": 57.692436,\"lng\": 11.975169,\"created_at\": \"2015-04-29 05:58:53\"}, {\"id\": 11,\"incident\": \"någon gick före i matkön\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-04-29 06:01:27\"}, {\"id\": 13,\"incident\": \"någon tog min bulle\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-04-29 14:59:55\"}, {\"id\": 15,\"incident\": \"jag fick en äcklig bulle\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-04-29 15:00:46\"}, {\"id\": 16,\"incident\": \"jävla äsch\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-01 13:08:19\"}, {\"id\": 17,\"incident\": \"Skivade fallossymboler\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-01 13:10:19\"}, {\"id\": 27,\"incident\": \"Funkar detta fortfarande? :P\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-04 07:40:11\"}, {\"id\": 28,\"incident\": \"greatfdss\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-05 05:22:35\"}, {\"id\": 29,\"incident\": \"ugfdrrgghjyrddfg\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-05 05:36:22\"}, {\"id\": 30,\"incident\": \"Wootwwoooot\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 02:57:54\"}, {\"id\": 31,\"incident\": \"testingtestingName\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 03:03:01\"}, {\"id\": 32,\"incident\": \"default? :)\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 03:05:42\"}, {\"id\": 33,\"incident\": \"gggg\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 03:33:10\"}, {\"id\": 34,\"incident\": \"gggg\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 03:38:01\"}, {\"id\": 35,\"incident\": \"Happy times :) (Bad thing to report here maybe?)\n" +
                    "\n" +
                    "ROWS!\",\"lat\": 57.687164,\"lng\": 11.949335,\"created_at\": \"2015-05-06 03:50:53\"}]";
            JSONArray incidents;
            //String stuffs = getTest.get();
            if(true){//TODO DEBUGGING stuffs != null){
                //Log.d("MapsActivity", );
                incidents = new JSONArray(str);
                LinkedList<LatLng> coords = new LinkedList<LatLng>();
                for(int i = 0; i < incidents.length(); i++ ){
                    double lat = incidents.getJSONObject(i).getDouble("lat");
                    double lng = incidents.getJSONObject(i).getDouble("lng");
                    Log.d("MapsActivity","lat: "+lat+" long: "+lng );
                    coords.add(new LatLng(lat, lng));
                }
                addMarkers(coords);

                //Get logs
                //extract each lat&lng into coords
            }

        } catch (JSONException e) { e.printStackTrace();}

    }
}
