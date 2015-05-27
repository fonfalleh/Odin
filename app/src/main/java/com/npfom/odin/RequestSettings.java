package com.npfom.odin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/*
    Class for setting up connections for communicating with the database.
 */
public class RequestSettings {
    // Paths for the database.
    private String postURL = "http://188.166.95.224/incident_manager/v1/incidents";
    private String getURL = "http://188.166.95.224/incident_manager/v1/incidents";
    // Authorization string for the server.
    private String authorization = "49GbMsQ9vXdPMryrupJZ";

    // Sets up a POST connection to the database.
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

    // Sets up a GET connection to the database.
    public HttpURLConnection getGETConnection() throws IOException {
        URL url = new URL(getURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", authorization);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(false);
        return conn;
    }
}
