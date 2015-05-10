package com.npfom.odin;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Falleh on 2015-05-10.
 */
public class LatLngHolder {
    private static LatLng latlng;
    private static LatLng liseberg = new LatLng(57.69448, 11.9928214); //Default value
    public static LatLng getLatLng(){
        if(latlng == null){
            return liseberg;
        } else {
            return latlng;
        }
    }
    public static void updateLatLng(Location loc){
        if(loc == null){
            latlng = liseberg;
        } else {
            latlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
    }
}
