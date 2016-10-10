package com.example.luan.adoptyourstreet.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hai on 26/08/2016.
 */
public class DayActivitive extends WeekActivitie {
   public ArrayList<PrayerUser> listPrayer=new ArrayList<>();

    public double distanceGoal;
    public long durationGoal;
    public Long startDate;

    public DayActivitive() {
    }

    public DayActivitive(double distanceGoal, long durationGoal, Long startDate) {
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
    public String DayId;

    public double sumDistance(){
        double sum=0;
        for(PrayerUser pr:listPrayer){
            sum+=pr.distance;
        }
        return sum;
    }

    public long sumDuration(){
        long sum=0;
        for(PrayerUser pr:listPrayer){
            sum+=(long) pr.duration;
        }
        return sum;
    }

    public void setLsetListPrayerOfDay(DataSnapshot data){
        DataSnapshot snPrayer= data.child("prayers");
        if(snPrayer.getChildrenCount()>0){
            for(DataSnapshot sNapP:snPrayer.getChildren()){
                PrayerUser p=sNapP.getValue(PrayerUser.class);
                p.id=sNapP.getKey();
                listPrayer.add(p);
            }
        }
    }

    @Override
    public int compareTo(Object another) {
        long compage=((WeekActivitie)another).startDate;
        return (int) (this.startDate-compage);
    }
    @Override
    public String toString() {
        return "DaykActivitie{" +
                "distanceGoal=" + distanceGoal +
                ", durationGoal=" + durationGoal +
                ", startDate=" + startDate +
                ", DayId='" + DayId + '\'' +
                '}';
    }


}
