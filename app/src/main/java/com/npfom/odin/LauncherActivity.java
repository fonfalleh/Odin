package com.npfom.odin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
    The activity that launches when the app starts. From here, one can go to either Report Incident,
    Show Incidents or Contact pages. (MainActivity, MapsActivity, and ContactActivity, respectively)
 */
public class LauncherActivity extends AppCompatActivity {

    // Graphical components.
    private Button reportActivityButton, mapsActivityButton, contactActivityButton;
    private TextView titleText;
    private int titlePresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //Get pointers to the buttons so that they can be disabled/enabled
        reportActivityButton = (Button) findViewById(R.id.reportActivityButton);
        mapsActivityButton = (Button) findViewById(R.id.mapsActivityButton);
        contactActivityButton = (Button) findViewById(R.id.contactActivityButton);

        titleText = (TextView) findViewById(R.id.launcherTitle);
    }

    //When returning to activity, make all buttons clickable again
    @Override
    protected void onResume() {
        super.onResume();
        reportActivityButton.setClickable(true);
        mapsActivityButton.setClickable(true);
        contactActivityButton.setClickable(true);
        titleText.setTextSize(30);
        titleText.setText("WELCOME TO \nTHE ODIN INITIATIVE");
        titleText.setClickable(true);
    }

    // Methods to open the three major activities
    // When a button is clicked, disable all buttons to prevent opening multiple activities at once
    public void openMainActivity(View view) {
        disableButtons();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMapsActivity(View view) {
        disableButtons();
        //   updateLocation();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openContactActivity(View view) {
        disableButtons();
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    // Easter egg
    public void titlePressed(View view) {
        titlePresses++;
        if (titlePresses > 10) {
            titleText.setTextSize(25);
            titleText.setText("HEY! I AIN'T NO BUTTON! STOP TOUCHING ME!");
            titleText.setClickable(false);
        }
    }
    // Disables buttons to prevent multiple activities being launched by mistake.
    private void disableButtons() {
        reportActivityButton.setClickable(false);
        mapsActivityButton.setClickable(false);
        contactActivityButton.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launcher, menu);
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
