package com.npfom.odin;

/**
 * Created by Kasper on 29-Apr-15.
 *
 * Simple wrapper class for latitude-longitude coordinates
 */
public class Coordinates {

    private double lat;
    private double lng;

    public Coordinates(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }


}
