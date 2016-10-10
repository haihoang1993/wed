package com.example.luan.adoptyourstreet.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.activities.EventShowCaseActivity;
import com.example.luan.adoptyourstreet.adapters.Event_Adapter;
import com.example.luan.adoptyourstreet.firebase.FirebaseCallBackEvent;
import com.example.luan.adoptyourstreet.firebase.FireBaseDatabaseManager;
import com.example.luan.adoptyourstreet.models.Event;
import com.example.luan.adoptyourstreet.models.ObjDemo;


import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hai on 06/07/2016.
 */
public class EventsFragment extends Fragment implements FirebaseCallBackEvent{

    public EventsFragment() {

    }

    int data;

    @SuppressLint("ValidFragment")
    public EventsFragment(int data) {
        this.data = data;
    }


    NiceSpinner spinner;


    ArrayList<Event> listEvent;
    Event_Adapter eventAdapter;
    ListView listViewEvent;

    @Override
    public void onStart() {
        super.onStart();
        init();
        FireBaseDatabaseManager.sharedInstance.setCallBackEvent(this);
        getEventByTopic("Love");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        spinner = (NiceSpinner) rootView.findViewById(R.id.spinnerEvent);
        setSpinner();
        listViewEvent = (ListView) rootView.findViewById(R.id.ListviewEvent);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        listEvent = new ArrayList<>();
        eventAdapter = new Event_Adapter(getActivity().getApplicationContext(), R.layout.item_list_event, listEvent) {
            @Override
            public void ClickBuy() {

            }

            @Override
            public int demo(int a) {
                return 0;
            }
        };
        listViewEvent.setAdapter(eventAdapter);

    }

    public void getEventByTopic(String topic) {
        listEvent.clear();
        eventAdapter.notifyDataSetChanged();
        FireBaseDatabaseManager.sharedInstance.getEventByTopic(topic);
    }

    private void setSpinner() {
        // Adding child data
        final List<String> item = new ArrayList<String>();
        item.add("Love");
        item.add("Neighbour");
        item.add("Family");
        item.add("School");
        spinner.attachDataSource(item);
        spinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getEventByTopic(item.get(position));
            }
        });

    }

    @Override
    public void OnEventAdded(Event event) {
        if (!checkHasEvent(event)) {
            listEvent.add(event);
            eventAdapter.notifyDataSetChanged();
        }
    }

    public boolean checkHasEvent(Event event) {
        for (Event e : listEvent) {
            if (e.id.equals(event.id)) {
                return true;

            }
        }
        return false;
    }
}


