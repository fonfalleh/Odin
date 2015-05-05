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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button reportButton = (Button) findViewById(R.id.reportButton);
        final RatingBar starBar = (RatingBar) findViewById(R.id.starBar);
        final EditText editComplaint = (EditText) findViewById(R.id.editComplaint);
        final EditText editName = (EditText) findViewById(R.id.editName);
        final TextView responseText = (TextView) findViewById(R.id.responseText);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);


        //Listener with definition of what should happen on button press, that is
        // send the entered data and current coordinates to the SQL server.
        View.OnClickListener dumbListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = starBar.getRating();
                System.out.println("Name: " + editName.getText());
                System.out.println("Complaint: " + editComplaint.getText());
                System.out.println("Rating:" + rating);
                responseText.clearComposingText();
                if (rating < 1.5) {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nGeez you whiner, get over it!");
                } else if (rating < 3.5) {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nCalling you mom now.");
                } else {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nSending SWAT-team to your location now.");
                }
                //TODO very temp
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double lat, lng;
                if(location != null) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                } else {
                    lat = 57.687163; // TODO test 57.687163, 11.949335 (Folkdansringen Göteborg i Slottskogen)
                    lng = 11.949335;
                }
                Coordinates cc = new Coordinates(lat, lng);
                //TODO end temp
                String complaint = "" +  editComplaint.getText();
                String parameters = "incident=" + complaint + "&lat=" + cc.getLat() + "&long=" + cc.getLng();
                new RequestManager().execute(parameters, "POST");

            }
        };
        reportButton.setOnClickListener(dumbListener);
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
     * @param view
     */
    public void checkGPS(View view) {
        Intent intent = new Intent(this, ShowLocationActivity.class);
        startActivity(intent);
    }
}
