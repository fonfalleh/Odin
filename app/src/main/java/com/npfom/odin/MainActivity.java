package com.npfom.odin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Members of the activity class
    private EditText editComplaint;
    private EditText editName;
    private TextView responseText;
    private LocationManager locationManager;
    private TextView timeView;
    private TextView dateView;
    private int time = -1;
    private int date = -1;
    private int todaysDate;
    private Button reportButton;
    private Button markerButton;

    // Coordinates in form of LatLng that deals with locations.
    // Current position (if available), initialized as some random place in Gothenburg.
    private LatLng currentLatLng;
    // Coordinates given from the MapMarkerActivity-activity.
    private LatLng reportLatLng;
    // Boolean that decides wether or not to use custom coorinates or gps coordinates when reporting an incident.
    private boolean useCustomCoordinates = false;

    //Constants to send to date and time activities to request the appropriate data as a result.
    static final int TIME_REQUEST = 1337;
    static final int DATE_REQUEST = 1338;
    static final int LOCATION_REQUEST = 5; //Chosen by fair dice roll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get pointers to Text-edit and Text-response fields.
        editComplaint = (EditText) findViewById(R.id.editComplaint);
        editName = (EditText) findViewById(R.id.editName);
        responseText = (TextView) findViewById(R.id.responseText);

        //Get pointers to buttons so that they can be disabled
        reportButton = (Button) findViewById(R.id.reportButton);
        markerButton = (Button) findViewById(R.id.markerButton);

        // Plus or minus 0.01 for both lat and long, to get some more dispersed tags ;)
        // To demonstrate app on emulator or phone without working GPS.
        // These get overwritten if actual coordinated can be found.
        Random rng = new Random();
        currentLatLng = new LatLng(57.687163 + ((rng.nextDouble() - 0.5) / 50),
                11.949335 + ((rng.nextDouble() - 0.5) / 50)); // Default value
        //Create location manager and use it to update the location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Tries to update the current position of the device.
        updateLocation();

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

    //Called on activity resume, makes all buttons clickable again.
    @Override
    protected void onResume(){
        super.onResume();
        enableButtons();
    }

    /**
     * Method for sending the user submitted incident data to the server.
     * @param view
     */
    public void sendReport(View view) {
        disableButtons();
        responseText.clearComposingText();

        //TODO Most of this info does not get sent to the database at the moment
        // but we can at least print it for the user! :)

        // Get the name. If no name is entered, default value is set.
        String name = "" + editName.getText();
        if (name.length() == 0) {
            name = "Anonymous";
        }
        // Get the incident report. If no report is entered, default value is set.
        String complaint = "" + editComplaint.getText();
        if (complaint.length() == 0) {
            complaint = "No description given";
        }

        //If the user hasn't entered a custom location, use the location from the GPS.
        if (!useCustomCoordinates) {
            reportLatLng = currentLatLng;
        }

        //Print the report information to the user.
        responseText.setText("Name: " + name);
        responseText.append("\nComplaint: " + complaint);
        responseText.append("\nDate: " + dateView.getText());
        responseText.append("\nTime: " + timeView.getText());
        responseText.append("\nCoordinates: " + reportLatLng.toString());

        //Send the report to the database using RequestManager
        //Have to check the timestamp parameter!!
        String parameters = "name=" + name + "&incident=" + complaint + "&lat=" + reportLatLng.latitude +
                "&long=" + reportLatLng.longitude + "&timestamp=" + timeView.getText();
        //TODO: ADD TIME, DATE and NAME to Database post request!!
        OdinTextView otw = new OdinTextView(responseText);
        new RequestManager(otw).execute(parameters, "POST");
        enableButtons();
    }
    
    //OnClick methods for the buttons in the Activity, to open other activities,
    // sometimes to get results
    public void openMarkerMap(View view){
        disableButtons();
        updateLocation();
        Intent intent = new Intent(this, MapMarkerActivity.class);
        intent.putExtra("lat", currentLatLng.latitude);
        intent.putExtra("lng", currentLatLng.longitude);
        startActivityForResult(intent, LOCATION_REQUEST);
    }

    /**
     * This method tries to fetch a new current location from the locationmanager. If if fails,
     * the new location object (which is null) is disregarded.
     */
    private void updateLocation(){
        Location tmpLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(tmpLocation != null){
            currentLatLng = new LatLng(
                    tmpLocation.getLatitude(),
                    tmpLocation.getLongitude()
            );
        }
    }

    public void pickDate(View view) {
        disableButtons();
        Intent intent = new Intent(this, DatePickerActivity.class);
        startActivityForResult(intent, DATE_REQUEST);
    }

    public void pickTime(View view) {
        disableButtons();
        Intent intent = new Intent(this, TimePickerActivity.class);
        startActivityForResult(intent, TIME_REQUEST);
    }

    //Method to handle receiving data back from another activity
    //Did not understand Uri's AT ALL, so used putExtra() instead :P
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check which kind of result we got back
        if (requestCode == TIME_REQUEST) {
            //Make sure the request was successful (it is not if the user pressed the "Back" button
            // instead of the "Done" button)
            if (resultCode == RESULT_OK) {
                int timeExtra = data.getIntExtra("TIME", -1);
                time = timeExtra;
                updateTime();
            }
        } else if (requestCode == DATE_REQUEST) {
            if (resultCode == RESULT_OK) {
                int dateExtra = data.getIntExtra("DATE", -1);
                //Check if chosen date is in the future, if so, reject it and keep the current date
                if (dateExtra > todaysDate) {
                    dateView.setTextSize(15);
                    dateView.setText("Cannot report incidents in the future");
                } else {
                    date = dateExtra;
                    updateDate();
                }
            }
        } else if (requestCode == LOCATION_REQUEST){
            if(resultCode == Activity.RESULT_OK) {
                double lat = data.getDoubleExtra("lat", 0);
                double lng = data.getDoubleExtra("lng", 0);
                Log.d("Main, getting marker", "lat:" + lat + " lng:" + lng);
                reportLatLng = new LatLng(lat, lng);
                useCustomCoordinates = true;
            }
        }
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

    //Methods to disable then re-enable all buttons, to prevent user from opening multiple
    // Activities, or instances of an Activity, at once
    private void disableButtons(){
        timeView.setClickable(false);
        dateView.setClickable(false);
        reportButton.setClickable(false);
        markerButton.setClickable(false);
    }

    private void enableButtons() {
        timeView.setClickable(true);
        dateView.setClickable(true);
        reportButton.setClickable(true);
        markerButton.setClickable(true);
    }

    //Method to convert month number as int to month name as String
    private String getMonth(int month) {
        return new DateFormatSymbols().getShortMonths()[month];
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
