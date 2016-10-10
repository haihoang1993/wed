package com.example.luan.adoptyourstreet.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.adapters.ExpandableListAdapter;
import com.example.luan.adoptyourstreet.adapters.ExpandableListAdapterActivity;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseManager;
import com.example.luan.adoptyourstreet.firebase.FirebaseCallBackInActivity;
import com.example.luan.adoptyourstreet.models.DayActivitive;
import com.example.luan.adoptyourstreet.models.Prayer;
import com.example.luan.adoptyourstreet.models.User;
import com.example.luan.adoptyourstreet.models.WeekActivitie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hai on 05/07/2016.
 */
public class ActivityFragment extends Fragment implements FirebaseCallBackInActivity {
    ExpandableListAdapterActivity listAdapter;
    ExpandableListView expListView;
    List<WeekActivitie> listWeek;

    public ActivityFragment(){

    }

    @SuppressLint("ValidFragment")
    public ActivityFragment(List<WeekActivitie> l){
       // listWeek=l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listWeek=new ArrayList<>();
        FireBaseDatabaseManager.sharedInstance.setCallBackInActivity(this);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_activity, container, false);
// get the listview
        expListView = (ExpandableListView)  rootView.findViewById(R.id.ExpLv);
        listAdapter = new ExpandableListAdapterActivity(rootView.getContext(),listWeek);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        FireBaseDatabaseManager.sharedInstance.getPrayOfWeekDayInActivity();
        return rootView;
    }



    @Override
    public void OnWeekActivitie(WeekActivitie weekActivitie) {
        listWeek.add(weekActivitie);
        listAdapter.notifyDataSetChanged();
    }


    /*
    Preparing the list data
    */

}
