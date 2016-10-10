package com.example.luan.adoptyourstreet.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by funkoigame on 27/09/2016.
 */

public class Event implements Comparable  {
    public String id;
    public String name;
    public String topic;
    public long createDate;
    public long fireDate;
    public double latitude;
    public double longitude;
    public String userId;
    public HashMap<String,String> userEvent;
    public Event() {

    }
    public Event(String id, String name, String topic, long createDate, long fireDate, double latitude, double longitude,String use ) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.createDate = createDate;
        this.fireDate = fireDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId=use;
    }
     public void setUserEvent(DataSnapshot dataUser){
         if(userEvent==null) userEvent=new HashMap<>();
          DataSnapshot data=  dataUser.child("users");
         Log.d("user of count:",data.getChildrenCount()+"");
         if(data.getChildrenCount()>0) {
             for (DataSnapshot u : data.getChildren()) {
                 userEvent.put(u.getKey(), u.getValue().toString());
             }
         }
     }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("topic",topic);
        result.put("createDate",createDate);
        result.put("fireDate",fireDate);
        result.put("latitude",latitude);
        result.put("longitude",longitude);
        result.put("userUid:",userId);
        return result;
    }

    public  String getStringTimeFire(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(fireDate*1000);
        Calendar timeCurent=Calendar.getInstance();
        if((calendar.getTime().getYear()==timeCurent.getTime().getYear())&&(calendar.getTime().getMonth()==timeCurent.getTime().getMonth())&&(calendar.getTime().getDate()==timeCurent.getTime().getDate())){

            SimpleDateFormat formatter
                    = new SimpleDateFormat ("hh:mm a");
            return formatter.format(calendar.getTime());
        } else {
            SimpleDateFormat formatter
                    = new SimpleDateFormat ("MMM d,yyyy");
            return formatter.format(calendar.getTime());
        }
    }

    public int getCountUserOfEven(){
       return userEvent.size();
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }
}