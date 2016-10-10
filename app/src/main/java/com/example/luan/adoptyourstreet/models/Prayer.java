package com.example.luan.adoptyourstreet.models;

import com.google.android.gms.maps.model.Marker;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by luan on 6/10/16.
 */
public class Prayer {

    public String avgSpeed;
    public Double distance;
    public Integer duration;
    public Double endDate;
    public Boolean hasImage;
    public Double lastLatitude;
    public Double lastLongitude;
    public Double maxSpeed;
    public String name;
    public String routeUid;
    public String userUid;

    public Prayer() {
    }
    public Prayer(String avgSpeed, Double distance, Integer duration, Double endDate, Boolean hasImage, Double lastLatitude, Double lastLongitude, Double maxSpeed, String name, String routeUid, String userUid, Marker marker) {
        this.avgSpeed = avgSpeed;
        this.distance = distance;
        this.duration = duration;
        this.endDate = endDate;
        this.hasImage = hasImage;
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.maxSpeed = maxSpeed;
        this.name = name;
        this.routeUid = routeUid;
        this.userUid = userUid;
        this.marker = marker;
        setSpeedMPH();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("avgSpeed", avgSpeed);
        result.put("distance", distance);
        result.put("duration", duration);
        result.put("endDate", endDate);
        result.put("hasImage", hasImage);
        result.put("lastLatitude", lastLatitude);
        result.put("lastLongitude", lastLongitude);
        result.put("maxSpeed", maxSpeed);
        result.put("name", name);
        result.put("routeUid", routeUid);
        result.put("userUid", userUid);

        return result;
    }
    public void setSpeedMPH(){
       long speed= Math.round(distance/duration* 2.2369362920544);
        avgSpeed=speed + " mph";
    }
    public Marker marker;
    public String uid;
}

