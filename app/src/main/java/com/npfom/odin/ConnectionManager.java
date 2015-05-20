package com.npfom.odin;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Kasper on 29-Apr-15.
 *
 * Class to handle connection to SQL server.
 * Has methods to send a report, delete a report and get all stored reports.
 *
 */
public class ConnectionManager extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        HttpURLConnection connection = null;
        String parameters = strings[0];

        //Create connection
        try {
            Log.d("ConnectionManager","1");

            url = new URL("http://188.166.95.224/incident_manager/v1/incidents");
            connection = (HttpURLConnection) url.openConnection();
            Log.d("ConnectionManager","2");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "49GbMsQ9vXdPMryrupJZ");
            Log.d("ConnectionManager","3");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            Log.d("ConnectionManager","4");
            try {

                Log.d("ConnectionManager","Second try started");
                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(parameters);
                wr.flush();
                wr.close();

                Log.d("ConnectionManager","Get response");
                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                Log.d("ConnectionManager","Almost at return!");
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();

                Log.d("ConnectionManager","Parameters: " + parameters);

         /*   Map<String,List<String>> map = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet()){
                for (String field : entry.getValue()){
                    System.out.println(field);
                }
            }
        */
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

    protected void onPostExecute(String result) {
        Log.d("ConnectionManager",result);
    }
}
