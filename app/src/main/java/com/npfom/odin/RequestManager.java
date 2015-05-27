package com.npfom.odin;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/*
    Class for communicating with the database.
 */
public class RequestManager extends AsyncTask<String, Void, String> {
    // Object to write output messages to.
    private RequestInterface objectToChange = null;

    public RequestManager(RequestInterface obj){
        objectToChange = obj;
    }


    // Method for sending requests to the database. Works in a background thread.
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        try {
            RequestSettings rs = new RequestSettings();
            // Checks what kind of connection is to be used.
            if (params[1].equals("POST")) {
                connection = rs.getPOSTConnection();
            } else if(params[1].equals("GET")) {
                connection = rs.getGETConnection();
            } else {
                return "Need a second argument, please. Get or Post";
            }

            // Send request
            if (params[1].equals("POST")) {
                DataOutputStream wr = new DataOutputStream(
                        connection.getOutputStream());
                // Writes the post request to the database.
                wr.writeBytes(params[0]);
                wr.flush();
                wr.close();
            }

               Log.d("Reponse", String.valueOf(connection.getResponseCode()));
               Log.d("ResMess", String.valueOf(connection.getResponseMessage()));

            // Get Response
            InputStream is;
            if(connection.getResponseCode() == 400) {
                is = connection.getErrorStream();
            } else {
                is = connection.getInputStream();
            }
            // Takes the returned stream and reads the data.
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            // Sends back the returned message.
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }
    // Updates the objectToChange with relevant message after getting a response from the server.
    @Override
    protected void onPostExecute(String result){
        Log.d("RequestManager","Result: " + result);
        objectToChange.process(result);
    }

    private void displayResult(String res) {

    }
}
