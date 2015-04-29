package com.npfom.odin;

import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kasper on 29-Apr-15.
 */
public class ConnectionManager {


    public String sendReport(String report, Coordinates cords) {
        URL url;
        HttpURLConnection connection = null;
        String parameters = "incident=" + report + "&lat=" + cords.getLat() + "&lng=" + cords.getLng();
        try {
            //Create connection
            url = new URL("htttp://188.166.95.224/incident_manager/v1/incidents");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", "49GbMsQ9vXdPMryrupJZ");

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(parameters);
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

    public Coordinates[] getReports() {

        return null;

    }


}
