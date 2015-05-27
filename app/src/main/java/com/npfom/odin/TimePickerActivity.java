package com.npfom.odin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;

/*
    Activity for choosing what time to use in the report.
 */
public class TimePickerActivity extends AppCompatActivity {

    TimePicker timeP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        // Get pointer to TimePicker view and set it to 24h mode (We are in Sweden! :D)
        timeP = (TimePicker) findViewById(R.id.timePicker);
        timeP.setIs24HourView(true);
    }

    // When user presses done, return the picked time to MainActivity so that time can be set,
    // then finish the activity.
    public void doneWithActivity(View view) {
        Intent result = new Intent();
        result.putExtra("TIME", timeP.getCurrentHour() * 100 + timeP.getCurrentMinute());
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_picker, menu);
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
