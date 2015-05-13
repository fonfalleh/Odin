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

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ActionBarActivity {

    EditText editComplaint;
    EditText editName;
    TextView responseText;
    private LocationManager locationManager;
    private String provider;
    TextView timeView;
    TextView dateView;
    int time = -1;
    int date = -1;
    int todaysDate;

    //Constants to send to date and time activities to request the appropriate data as a result.
    static final int TIME_REQUEST = 1337;
    static final int DATE_REQUEST = 1338;
    static final int LOCATION_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get pointers to Text-edit and Text-response fields.
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

        //Get pointers to date and time fields
        timeView = (TextView) findViewById(R.id.showTime);
        dateView = (TextView) findViewById(R.id.showDate);

        // If it's the first time the app starts, get current time and date
        // Time won't update after the app is first opened, which is the intended functionality,
        // since we want the report time to be as close to the incident (and thus the app opening)
        // as possible.
        if (time == -1 && date == -1) {
            Calendar cal = new GregorianCalendar();

            time = cal.get(Calendar.HOUR_OF_DAY) * 100 + cal.get(Calendar.MINUTE);
            date = cal.get(Calendar.YEAR) * 10000 + cal.get(Calendar.MONTH) * 100 + cal.get(Calendar.DAY_OF_MONTH);
            //Used to make sure the user doesn't enter a date in the future
            todaysDate = date;
            updateDate();
            updateTime();
        }
    }

    public void sendReport(View view) {
        responseText.clearComposingText();

        //Most of this info does not get sent to the database at the moment
        // but we can at least print it for the user! :)
        if (editName.getText().length() == 0) {
            responseText.setText("Name: Anonymous");
        } else {
            responseText.setText("Name: " + editName.getText());
        }
        responseText.append("\nComplaint: " + editComplaint.getText());
        responseText.append("\nDate: " + getMonth((date / 100) % 100) + " " + (date % 100) + ", " + (date / 10000));
        responseText.append("\nTime: " + String.format("%02d:%02d", time / 100, time % 100));

        //TODO very temp
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Test: 57.687163, 11.949335 (Folkdansringen Göteborg i Slottskogen)
        // Plus or minus 0.01 for both lat and long, to get some more dispersed tags ;)
        // To demonstrate app on emulator or phone without working GPS.
        // These get overwritten if actual coordinated can be found.
        Random rng = new Random();
        Coordinates cc = new Coordinates(57.687163 + ((rng.nextDouble() - 0.5) / 50),
                11.949335 + ((rng.nextDouble() - 0.5) / 50));

        if (location != null) {
            cc.setNewCoordinates(location.getLatitude(), location.getLongitude());
            responseText.append("\nCoordinates: " + cc.toString());
        } else {
            responseText.append("\nCould not connect to GPS.\nUsing predefined coordinates.");
        }
        //TODO end temp
        String complaint = "" + editComplaint.getText();
        String parameters = "incident=" + complaint + "&lat=" + cc.getLat() + "&long=" + cc.getLng();
        OdinTextView otw = new OdinTextView(responseText);
        new RequestManager(otw).execute(parameters, "POST");
    }

    public void checkGPS(View view) {
        Intent intent = new Intent(this, ShowLocationActivity.class);
        startActivity(intent);
    }

    public void openMap(View view){
        updateLocation();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void openMarkerMap(View view){
        updateLocation();
        Intent intent = new Intent(this, MapMarker.class);
        startActivityForResult(intent, LOCATION_REQUEST);
    }
    private void updateLocation(){
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //provider
        LatLngHolder.updateLatLng(location);
    }

    public void pickDate(View view) {
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_REQUEST);
    }

    public void pickTime(View view) {
        Intent intent = new Intent(this, TimePickerActivity.class);
        startActivityForResult(intent, TIME_REQUEST);
    }

    //Method to handle receiving data back from another activity
    //Did not understand Uri's AT ALL, so used putExtra() instead :P
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int timeExtra = data.getIntExtra("TIME", -1);
        int dateExtra = data.getIntExtra("DATE", -1);

        //Check which kind of result we got back
        if (requestCode == TIME_REQUEST) {
            //Make sure the request was successful (not sure if needed but what the hell)
            if (resultCode == RESULT_OK) {
                time = timeExtra;
                updateTime();
            }
        } else if (requestCode == DATE_REQUEST) {
            if (resultCode == RESULT_OK) {
                //Check if chosen date is in the future, if so, reject it.
                if (dateExtra > todaysDate) {
                    dateView.setTextSize(15);
                    dateView.setText("Cannot report incidents in the future");
                } else {
                    date = dateExtra;
                    updateDate();
                }
            }
        } else if (requestCode == 5){
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            Log.d("Main, getting marker", "lat:" + lat + " lng:" +lng );
            //TODO make more pretty and useful
            //TODO Use the coordinates.
        }
    }

    //Method to convert month number as int to month name as String
    public String getMonth(int month) {
        return new DateFormatSymbols().getShortMonths()[month];
    }

    //Methods to update date and time views.
    private void updateTime() {
        if (time == -1) {
            timeView.setText("ERROR");
        } else {
            timeView.setText(String.format("%02d:%02d", time / 100, time % 100));
        }
    }

    private void updateDate() {
        dateView.setTextSize(25);
        if (date == -1) {
            dateView.setText("ERROR");
        } else {
            dateView.setText(getMonth((date / 100) % 100) + " " + (date % 100) + ", " + (date / 10000));
        }
    }

    //Base methods, auto-implemented by Android Studio
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
}
