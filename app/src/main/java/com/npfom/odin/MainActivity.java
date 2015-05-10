package com.npfom.odin;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends ActionBarActivity {

    EditText editComplaint;
    EditText editName;
    TextView responseText;
    TextClock clock;
    TextView date;

    //Constants to send to date and time activities to request the appropriate data as a result.
    static final int TIME_REQUEST = 1337;
    static final int DATE_REQUEST = 80085;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get pointers to Text-edit and Text-response fields.
        editComplaint = (EditText) findViewById(R.id.editComplaint);
        editName = (EditText) findViewById(R.id.editName);
        responseText = (TextView) findViewById(R.id.responseText);

        //Get pointers to date and time fields
        clock = (TextClock) findViewById(R.id.showClock);
        date = (TextView) findViewById(R.id.showDate);

        //Set the date view to the current date, supplied by Calendar
        Calendar cal = new GregorianCalendar();
        date.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.UK) +
                " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR));

    }

    public void sendReport(View view) {
        responseText.clearComposingText();

        //Name does not actually get sent to database at the moment
        //as the value of this functionality is questionable
        //but we can at least print it for the user! :)
        if (editName.getText().equals("")) {
            responseText.setText("Name: Anonymous");
        } else {
            responseText.setText("Name: " + editName.getText());
        }
        responseText.append("\nComplaint: " + editComplaint.getText());
        //TODO very temp
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        // Test: 57.687163, 11.949335 (Folkdansringen Göteborg i Slottskogen)
        // Plus or minus 0.01 for both lat and long, to get some more dispersed tags ;)
        // To demonstrate app on emulator or phone without working GPS.
        // These get overwritten if actual coordinated can be found.
        Random rng = new Random();
        Coordinates cc = new Coordinates(57.687163 + ((rng.nextDouble()-0.5)/50),
                11.949335 + ((rng.nextDouble()-0.5)/50));

        if (location != null) {
            cc.setNewCoordinates(location.getLatitude(), location.getLongitude());
            responseText.append("\nCoordinates: " + cc.toString());
        } else {
            responseText.append("\nCould not connect to GPS.\nUsing predefined coordinates.");
        }
        //TODO end temp
        String complaint = "" + editComplaint.getText();
        String parameters = "incident=" + complaint + "&lat=" + cc.getLat() + "&long=" + cc.getLng();
        new RequestManager().execute(parameters, "POST");
    }

    //Methods that get called to open other activities in the application, when the corresponding
    // button or field in the interface gets clicked! Date and Time activities start with a
    // response code that gets received in the onActivityResult-method.

    public void checkGPS(View view) {
        Intent intent = new Intent(this, ShowLocationActivity.class);
        startActivity(intent);
    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
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
    //Din not understand Uri's AT ALL, so used putExtra() instead :P
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        int timeExtra = data.getIntExtra("TIME", -1);
        String dateExtra = data.getStringExtra("DATE");
        Log.d("Main Result: ", "" + timeExtra);

        if (timeExtra != -1) {
            //If we got a time result
            Log.d("Got inside first IF", "YAY");
            //Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("Result in OK: ", "" + timeExtra);

                int hour = timeExtra / 100;
                int minute = timeExtra % 100;

                Log.d("InB4 clock.setText:", hour + ":" + minute);
                clock.setText(hour + ":" + minute);
            }
        } else if (dateExtra != null) {
            //Make sure the request was successful
            if (resultCode == RESULT_OK) {



            }
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
