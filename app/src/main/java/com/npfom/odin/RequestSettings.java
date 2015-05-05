package com.npfom.odin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bjornahlander on 15-05-05.
 */
public class RequestSettings {
    private String postURL = "http://188.166.95.224/incident_manager/v1/incidents";
    private String authorization = "49GbMsQ9vXdPMryrupJZ";
    private String getURL = "http://188.166.95.224/incident_manager/v1/incidents";

    /*
    Surround with try-catch
    close connection on your end
     */
    public HttpURLConnection getPOSTConnection() throws IOException {
        URL url = new URL(postURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", authorization);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    public HttpURLConnection getGETConnection() throws IOException {
        URL url = new URL(getURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", authorization);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

}
