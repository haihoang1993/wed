package com.example.luan.adoptyourstreet.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hai on 17/08/2016.
 */
public class User  {

    public String name;
    public int numPrayer;
    public Double totalDistance;
    public Integer totalDuration;

    public User(){
        numPrayer=0;
        totalDistance=0d;
        totalDuration=0;
    }

    public User(String Name, int numPrayer, Double totalDistance, Integer totalDuration) {
//        this.userId = userId;
        this.name= Name;
        this.numPrayer = numPrayer;
        this.totalDistance =(double) totalDistance;
        this.totalDuration = totalDuration;
    }


    public Map<String, Object> toMap() {
         HashMap<String, Object> result = new HashMap<>();
         result.put("name",name);
         result.put("numPrayer", numPrayer);
         result.put("totalDistance", totalDistance);
         result.put("totalDuration", totalDuration);

        return result;
    }

    @Override
    public String toString() {
        return "User id:"+userId+",name:"+name;
    }

    public String userId;
}
