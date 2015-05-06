package com.npfom.odin;

/**
 * Created by Kasper on 29-Apr-15.
 *
 * Simple class for latitude-longitude coordinates
 */
public class Coordinates {

    private double lat;
    private double lng;

    //Constructor
    public Coordinates(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public void setNewCoordinates(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String toString (){
        return "Lat." + lat + " Long. " + lng;
    }


}
