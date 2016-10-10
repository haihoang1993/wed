package com.example.luan.adoptyourstreet.models;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hai on 24/08/2016.
 */
public class WeekActivitie implements Comparable {
    public ArrayList<DayActivitive> listDateOfWeek=new ArrayList<>();
    public double distanceGoal;
    public long durationGoal;
    public long startDate;
    public String weekId;
    public WeekActivitie() {
    }

    public WeekActivitie(double distanceGoal, long durationGoal, long startDate) {
        this.distanceGoal = distanceGoal;
        this.durationGoal = durationGoal;
        this.startDate = startDate;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("distanceGoal",(double)distanceGoal);
        result.put("durationGoal",(long) durationGoal);
        result.put("startDate",(long) startDate);
        return result;
    }

    public void setListDayActivity(DataSnapshot dataSnapshot){
        DataSnapshot data=dataSnapshot.child("dateActivities");
        if(data.getChildrenCount()>0){
            for(DataSnapshot spItemday:data.getChildren()){
                DayActivitive d=spItemday.getValue(DayActivitive.class);
                d.DayId=spItemday.getKey();
                d.setLsetListPrayerOfDay(spItemday);
                listDateOfWeek.add(d);
                Collections.sort(listDateOfWeek);
            }
        }
    }


    public double sumDistance(){
        double sum=0;
        for(DayActivitive pr:listDateOfWeek){
            sum+=pr.sumDistance();
        }
        return sum;
    }

    public long sumDuration(){
        long sum=0;
        for(DayActivitive pr:listDateOfWeek){
            sum+=pr.sumDuration();
        }
        return sum;
    }
    @Override
    public int compareTo(Object another) {
        long compage=((WeekActivitie)another).startDate;
        return (int) (this.startDate-compage);
    }
    @Override
    public String toString() {
        return "WeekActivitie{" +
                "distanceGoal=" + distanceGoal +
                ", durationGoal=" + durationGoal +
                ", startDate=" + startDate +
                ", weekId='" + weekId + '\'' +
                '}';
    }


}
