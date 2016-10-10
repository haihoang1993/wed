package com.example.luan.adoptyourstreet.firebase;

import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.Event;
import com.example.luan.adoptyourstreet.models.Prayer;

import com.example.luan.adoptyourstreet.models.PrayerUser;
import com.example.luan.adoptyourstreet.models.User;
import com.example.luan.adoptyourstreet.models.WeekActivitie;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.location.Location;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by luan on 6/10/16.
 */

public class FireBaseDatabaseManager {

    DatabaseReference database;
    DatabaseReference prayersRef;
    DatabaseReference eventRef;
    DatabaseReference userRef;
    DatabaseReference WeekActivitiesUser;
    DatabaseReference DateActivitiesUser;
    ChildEventListener prayersListener;
    ValueEventListener userListener;
    ChildEventListener weekActitieListener;
    ChildEventListener dayActivitilListener;

    public static FireBaseDatabaseManager sharedInstance = new FireBaseDatabaseManager();

    private static String TAG = "FireBaseDatabaseManager";
    FireBaseDatabaseCallback delegate = null;
    FirebaseCallBackEvent callBackEvent=null;
    FirebaseCallBackInActivity callBackInActivity=null;
    FireBaseDatabaseManager() {
        database = FirebaseDatabase.getInstance().getReference();
        Log.w(TAG, "FireBaseDatabaseManager");

        prayersListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Prayer prayer = dataSnapshot.getValue(Prayer.class);
                prayer.uid = dataSnapshot.getKey();
                Log.w(TAG, "onChildAdded:" + prayer.uid);
                if (delegate != null) {
                    delegate.OnPrayerAdded(prayer);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.w(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.w(TAG, "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.w(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        };
        userListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User use=dataSnapshot.getValue(User.class);
                if(use!=null){
                      if (delegate != null) {
                        delegate.OnUserUpdated(use);
                    }
                } else {
                    if (delegate != null) {
                        User u=new User();
                        u.name="Anonymous";
                        u.numPrayer=0;
                        u.totalDistance=0.0;
                        u.totalDuration=0;
                        delegate.OnUserUpdated(u);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        weekActitieListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                WeekActivitie w=dataSnapshot.getValue(WeekActivitie.class);
                if(w==null){
                    Log.d("Week null:","null");
                }
                else {
                    w.weekId=dataSnapshot.getKey();
                    w.setListDayActivity(dataSnapshot);
                    delegate.OnWeekActivitie(w);
                }


            }        @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
    }

    public void getPrayerReference(){
        prayersRef = database.child("prayers");
    }

    public void getUserReference(String uid) {
        userRef = database.child("users").child(uid);
        if (userRef != null) {
            Log.w(TAG, "getUserReference: Success:"+userRef.child(uid));
            WeekActivitiesUser = database.child("users").child(uid).child("weekActivities");
            WeekActivitiesUser.hashCode();

        } else {
            Log.w(TAG, "getUserReference: Fail");
        }
    }

    public void getUserFromPrayer(final Prayer prayer){
        DatabaseReference user=database.child("users").child(prayer.userUid);
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User use=dataSnapshot.getValue(User.class);
                use.userId=prayer.userUid;
                delegate.showUserFromPrayer(use,prayer);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPrayOfWeekDayInActivity(){
       ChildEventListener weekActitieListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                WeekActivitie w=dataSnapshot.getValue(WeekActivitie.class);
                if(w==null){
                    Log.d("Week null:","null");
                }
                else {
                    w.weekId=dataSnapshot.getKey();
                    w.setListDayActivity(dataSnapshot);
                    callBackInActivity.OnWeekActivitie(w);
                }


            }        @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
        WeekActivitiesUser.removeEventListener(weekActitieListener);
        WeekActivitiesUser.addChildEventListener(weekActitieListener);
    }

    public void getEventByTopic(String topic){
        Query query=  database.child("events").orderByChild("topic").equalTo(topic);
        ChildEventListener eventChild=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event ev=dataSnapshot.getValue(Event.class);
                ev.id=dataSnapshot.getKey();
                ev.setUserEvent(dataSnapshot);
                callBackEvent.OnEventAdded(ev);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.removeEventListener(eventChild);
        query.addChildEventListener(eventChild);
    }

    public void setCallBackEvent(FirebaseCallBackEvent callback){
        this.callBackEvent=callback;
    }
     public void setCallBackInActivity(FirebaseCallBackInActivity callback){
         this.callBackInActivity=callback;
     }
    public void setFireBaseDatabaseDelegate(FireBaseDatabaseCallback delegate) {
        this.delegate = delegate;
    }

    public void getEventWeek(){
        WeekActivitiesUser.removeEventListener(weekActitieListener);
        WeekActivitiesUser.addChildEventListener(weekActitieListener);
    }

    public void addEvent() {
        prayersRef.removeEventListener(prayersListener);
        prayersRef.addChildEventListener(prayersListener);
        WeekActivitiesUser.removeEventListener(weekActitieListener);
        WeekActivitiesUser.addChildEventListener(weekActitieListener);
        userRef.removeEventListener(userListener);
        userRef.addListenerForSingleValueEvent(userListener);
    }

    public void removeEvent() {
        Log.i(TAG, "Remove Event Listener");
        prayersRef.removeEventListener(prayersListener);
        userRef.removeEventListener(userListener);
        WeekActivitiesUser.removeEventListener(weekActitieListener);
    }

    public void saveUser(User user){
        DatabaseReference u=userRef.push();
        user.userId=u.getKey();
        Map<String, Object> postValues = user.toMap();
        userRef.updateChildren(postValues);
    }

    public void getDayByWeek(WeekActivitie week){
        String idWeek=week.weekId;
        ChildEventListener enventDay= new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DayActivitive d=dataSnapshot.getValue(DayActivitive.class);
                d.DayId=dataSnapshot.getKey();
                delegate.OnDayOfWeekActivive(d);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        DateActivitiesUser=WeekActivitiesUser.child(idWeek).child("dateActivities");
        DateActivitiesUser.removeEventListener(enventDay);
        DateActivitiesUser.addChildEventListener(enventDay);

    }
    public void getCheckWeekByTime(Calendar c){

            Query query=WeekActivitiesUser.orderByChild("startDate").equalTo(-c.getTimeInMillis());

            ChildEventListener childEventListener= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    WeekActivitie w=dataSnapshot.getValue(WeekActivitie.class);
                    w.weekId=dataSnapshot.getKey();
                    delegate.OnGetWeekCheck(w);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
                    query.removeEventListener(childEventListener);
                    query.addChildEventListener(childEventListener);
    }
    public void getCheckDateByTimeAndWeek(String idWeek,Calendar c){
            Query query=WeekActivitiesUser.child(idWeek).child("dateActivities").orderByChild("startDate").equalTo(-c.getTimeInMillis());
            ChildEventListener childEventListener= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    DayActivitive w=dataSnapshot.getValue(DayActivitive.class);
                    w.DayId=dataSnapshot.getKey();
                    delegate.OnGetDayCheck(w);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
                    query.removeEventListener(childEventListener);
                    query.addChildEventListener(childEventListener);
    }


    public void addDateActiviies(String idWeek,double distanceGoal,long durationGoal,long startDate){
        DatabaseReference data=WeekActivitiesUser.child(idWeek).child("dateActivities").push();
        data.child("distanceGoal").setValue(distanceGoal);
        data.child("durationGoal").setValue(durationGoal);
        data.child("startDate").setValue(startDate);
    }

    public void AddlocaltionPoint(String idWeek, String idDate, Location location){
        DatabaseReference data=WeekActivitiesUser.child(idWeek).child("dateActivities").child(idDate).child("prayers").push();
        DatabaseReference point=data.child("locationPoints").push();
        point.child("altitude").setValue(location.getAccuracy());
        point.child("latitude").setValue(location.getLatitude());
        point.child("longitude").setValue(location.getLongitude());
        point.child("longitude").setValue(location.getLongitude());
        Calendar ca=Calendar.getInstance();
        point.child("timestamp").setValue(ca.getTimeInMillis());
        data.child("startDate").setValue(ca.getTimeInMillis());
        delegate.StartPrayer(data.getKey());

   }
    public void addweekActivities(double distanceGoal,long durationGoal,long startDate){
            DatabaseReference newWeek=WeekActivitiesUser.push();
            newWeek.child("distanceGoal").setValue(distanceGoal);
            newWeek.child("durationGoal").setValue(durationGoal);
            newWeek.child("startDate").setValue(startDate);
    }


    public void savePrayerUser(String idWeek,String idDay, PrayerUser prayer ){
        DatabaseReference data=WeekActivitiesUser.child(idWeek).child("dateActivities").child(idDay).child("prayers").child(prayer.id);
        data.child("avgSpeed").setValue(prayer.avgSpeed);
        data.child("distance").setValue(prayer.distance);
        data.child("duration").setValue(prayer.duration);
        data.child("endDate").setValue(prayer.endDate);
        data.child("maxSpeed").setValue(prayer.maxSpeed);
        data.child("prayerId").setValue(prayer.prayerId);
    }

    public void savePrayer(Prayer prayer) {
        DatabaseReference newPrayerRef = prayersRef.push();
        prayer.uid = newPrayerRef.getKey();
        Map<String, Object> postValues = prayer.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + newPrayerRef.getKey(), postValues);
        prayersRef.updateChildren(childUpdates);
    }

    public void saveEvent(Event event){
        DatabaseReference eventRef=database.child("events");
        DatabaseReference newEvent=eventRef.push();
        event.id=newEvent.getKey();
        Map<String, Object> postValue=event.toMap();
        Map<String,Object> childUpdate=new HashMap<>();
        childUpdate.put("/"+newEvent.getKey(),postValue);
        eventRef.updateChildren(childUpdate);
    }

    public void setPrayerHasImage(Prayer prayer, Boolean hasImage) {
        prayersRef.child(prayer.uid).child("hasImage").setValue(hasImage);
    }

    public void setImagesEvent(Event event){
        DatabaseReference eventRef=database.child("events");
        eventRef.child(event.id).child("imageId").setValue(event.id);
    }
}
