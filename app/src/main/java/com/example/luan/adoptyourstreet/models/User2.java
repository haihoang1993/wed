package com.example.luan.adoptyourstreet.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hai on 17/08/2016.
 */
public class User2 {

    public String name;
    public int numPrayer;
    public Double totalDistance;
    public Integer totalDuration;

    public User2(){

    }

    public User2(String name, int numPrayer, Double totalDistance, Integer totalDuration) {
//        this.userId = userId;+
        this.name= name;
        this.numPrayer = numPrayer;
        this.totalDistance =(double) totalDistance;
        this.totalDuration = totalDuration;
    }


    public Map<String, Object> toMap() {
         HashMap<String, Object> result = new HashMap<>();
         result.put("name",name);
         result.put("numPrayer", numPrayer);
         result.put("totalDistance",(double) totalDistance);
         result.put("totalDuration", totalDuration);

        return result;
    }

}
