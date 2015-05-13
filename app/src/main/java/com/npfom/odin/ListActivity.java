package com.npfom.odin;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class ListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        Button coordinatesButton = (Button) findViewById(R.id.coordinatesButton);
        final ListView listView = (ListView) findViewById(R.id.listView);
        final Button myButton = (Button) findViewById(R.id.my_button);
        final TextView textView = (TextView) findViewById(R.id.textView2);


        View.OnClickListener coordinatesListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Knappen fungerar");
                //listView. addFooterView(myButton);
                textView.setTextColor(0);
            }
        };
        coordinatesButton.setOnClickListener(coordinatesListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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
