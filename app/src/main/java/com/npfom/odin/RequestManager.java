package com.npfom.odin;

import android.os.AsyncTask;
import android.widget.TextView;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by bjornahlander on 15-05-05.
 */
public class RequestManager extends AsyncTask<String, Void, String> {
    private TextView textArea;

    public RequestManager(TextView text){
        textArea = text;

    }
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        try {
            RequestSettings rs = new RequestSettings();
            if (params[1].equals("POST")) {
                connection = rs.getPOSTConnection();
            } else if(params[1].equals("GET")) {
                connection = rs.getGETConnection();
            } else {
                return "Need a second argument, please. Get or Post";
            }


            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(params[0]);
            wr.flush();
            wr.close();

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
    @Override
    protected void onPostExecute(String result){
        Log.d("RequestManager","Result: " + result);
        textArea.setText(result);

    }

    private void displayResult(String res) {

    }
}
