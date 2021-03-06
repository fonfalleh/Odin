package com.npfom.odin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

/*
    Activity for picking a date to report.
 */
public class DatePickerActivity extends AppCompatActivity {

    // Date to report.
    DatePicker dateP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datepicker);
        dateP = (DatePicker) findViewById(R.id.datePicker);
    }

    // When we are done picking a date, this method returns the date to the previous activity.
    public void doneWithActivity(View view) {
        Intent result = new Intent();
        result.putExtra("DATE", dateP.getDayOfMonth() + dateP.getMonth() * 100 + dateP.getYear() * 10000);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_picker, menu);
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
