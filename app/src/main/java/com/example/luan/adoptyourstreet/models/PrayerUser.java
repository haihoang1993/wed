package com.example.luan.adoptyourstreet.models;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by funkoigame on 23/09/2016.
 */

public class PrayerUser {
    public String id;
    public String avgSpeed;
    public double distance;
    public long duration;
    public double endDate;
    public double maxSpeed;
     public String prayerId;
    public PrayerUser() {
    }
    public PrayerUser(String avgSpeed, double distance, long  duration, double endDate, double maxSpeed,  String prayerId) {
        this.avgSpeed = avgSpeed;
        this.distance = distance;
        this.duration = duration;
        this.endDate = endDate;
        this.maxSpeed = maxSpeed;
        this.prayerId = prayerId;
        setSpeedMPH();
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("avgSpeed", avgSpeed);
        result.put("distance", distance);
        result.put("duration", duration);
        result.put("endDate", endDate);
        result.put("maxSpeed", maxSpeed);
        result.put("prayerId",prayerId);
        return result;
    }
    public void setSpeedMPH(){
        long speed= Math.round(distance/duration* 2.2369362920544);
        avgSpeed=speed + " mph";
    }

    @Override
    public String toString() {
        return "PrayerUser{" +
                "id='" + id + '\'' +
                ", duration=" + duration +
                ", distance=" + distance +
                '}';
    }
}
