package com.npfom.odin;

import android.os.AsyncTask;

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
            System.out.println("1");

            url = new URL("http://188.166.95.224/incident_manager/v1/incidents");
            connection = (HttpURLConnection) url.openConnection();
            System.out.println("2");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "49GbMsQ9vXdPMryrupJZ");
            System.out.println("3");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            System.out.println("4");
            try {

                System.out.println("Second try started");
                //Send request
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                wr.writeBytes(parameters);
                wr.flush();
                wr.close();

                System.out.println("Get response");
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
                System.out.println("Almost at return!");
                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("Parameters: " + parameters);

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
        System.out.println(result);
    }


    public Coordinates[] getReports() {

        return null;

    }
}
