package com.npfom.odin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.List;

//Vad händer när det creatas
//Vad händer när man klickar på saker

public class ContactActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final Button phoneButton = (Button) findViewById(R.id.phoneButton);
        final Button webPageButton = (Button) findViewById(R.id.webPageButton);

        View.OnClickListener phone = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Nu fungerar telefonen!");

                // Build the intent
                Uri number = Uri.parse("tel:+46737008169");
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);

                // Verify it resolves
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(callIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // Start an activity if it's safe
                if (isIntentSafe) {
                    startActivity(callIntent);
                }
            }
        };
        phoneButton.setOnClickListener(phone);


        View.OnClickListener webPage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Nu fungerar websidan!");

                // Build the intent
                Uri webpage = Uri.parse("http://www.bris.se");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

                // Verify it resolves
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                // Start an activity if it's safe
                if (isIntentSafe) {
                    startActivity(webIntent);
                }
            }
        };
        webPageButton.setOnClickListener(webPage);







    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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
