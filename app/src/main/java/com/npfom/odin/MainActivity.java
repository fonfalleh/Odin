package com.npfom.odin;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find all interface elements
        Button reportButton = (Button) findViewById(R.id.reportButton);
        Button mapButton = (Button) findViewById(R.id.mapButton);
        //These two buttons not yet used
        Button gpsButton = (Button) findViewById(R.id.gpsButton);
        Button timeButton = (Button) findViewById(R.id.timeButton);

        //Elements to be modified in onClick-methods need to be declared final (Why?)
        final EditText editComplaint = (EditText) findViewById(R.id.editComplaint);
        final EditText editName = (EditText) findViewById(R.id.editName);
        final TextView responseText = (TextView) findViewById(R.id.responseText);


        //Create listeners for all the buttons

        //Listener for the "Report"-button, with definition of what should happen on button press,
        // that is send the entered data and current coordinates to the SQL server.
        View.OnClickListener reportListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                new RequestManager().execute(parameters, "POST");
            }
        };

        // Create listener for the "GPS"-button
        View.OnClickListener gpsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseText.setText("GPS!");
            }
        };

        // Create listener for the "TIME"-button
        View.OnClickListener timeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseText.setText("TIME!");
            }
        };

        //Set appropriate listeners for all buttons
        reportButton.setOnClickListener(reportListener);
        gpsButton.setOnClickListener(gpsListener);
        timeButton.setOnClickListener(timeListener);
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

    /**
     * Testing launching other activities
     *
     * @param view
     */
    public void checkGPS(View view) {
        Intent intent = new Intent(this, ShowLocationActivity.class);
        startActivity(intent);
    }
    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
