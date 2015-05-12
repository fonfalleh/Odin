package com.npfom.odin;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ActionBarActivity {

    EditText editComplaint;
    EditText editName;
    TextView responseText;
    private LocationManager locationManager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Elements to be modified in onClick-methods need to be declared final (Why?)
        editComplaint = (EditText) findViewById(R.id.editComplaint);
        editName = (EditText) findViewById(R.id.editName);
        responseText = (TextView) findViewById(R.id.responseText);

        //TODO testing shared coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        updateLocation(); //TODO Does not use provider anymore, might be null...

    }

    public void sendReport(View view) {
        responseText.clearComposingText();

        //Name does not actually get sent to database at the moment
        //as the value of this functionality is questionable
        //but we can at least print it for the user! :)
        if (editName.getText().length() == 0) {
            responseText.setText("Name: Anonymous");
        } else {
            responseText.setText("Name: " + editName.getText());
        }
        responseText.append("\nComplaint: " + editComplaint.getText());
        //TODO very temp
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // TODO test 57.7072189,11.9670495 (Gustaf Adlofs torg)
        // Test: 57.687163, 11.949335 (Folkdansringen Göteborg i Slottskogen)
        // To demonstrate app on emulator or phone without working GPS.
        // These get overwritten if actual coordinated can be found.
        Coordinates cc = new Coordinates(57.687163, 11.949335);

        if (location != null) {
            cc.setNewCoordinates(location.getLatitude(), location.getLongitude());
            responseText.append("\nCoordinates: " + cc.toString());
        } else {
            responseText.append("\nCould not connect to GPS.\nUsing predefined coordinates.");
        }
        //TODO end temp
        String complaint = "" + editComplaint.getText();
        String parameters = "incident=" + complaint + "&lat=" + cc.getLat() + "&long=" + cc.getLng();
        new RequestManager(responseText).execute(parameters, "POST");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void openMap(View view){
        updateLocation();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void openMarkerMap(View view){
        updateLocation();
        Intent intent = new Intent(this, MapMarker.class);
        //startActivity(intent);
        int requestcode = 5; // A fair diceroll
        startActivityForResult (intent, requestcode);
    }
    private void updateLocation(){
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //provider
        LatLngHolder.updateLatLng(location);
    }
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if (requestCode == 5){
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            Log.d("Main, getting marker", "lat:" + lat + " lng:" +lng );

        }
    }
}
