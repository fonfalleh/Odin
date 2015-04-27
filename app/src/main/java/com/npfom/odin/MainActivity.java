package com.npfom.odin;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

        View.OnClickListener dumbListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = starBar.getRating();
                System.out.println("Name: " + editName.getText());
                System.out.println("Complaint: \n" + editComplaint.getText());
                System.out.println("\nRating:" + rating);
                responseText.clearComposingText();
                if (rating < 1.5) {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nGeez you whiner, get over it!");
                } else if (rating < 3.5) {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nCalling you mom now.");
                } else {
                    responseText.setText("Your complaint of " + rating + "stars has been registered. \nSending SWAT-team to your location now.");
                }
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


}
