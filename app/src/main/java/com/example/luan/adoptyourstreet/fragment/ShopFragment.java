package com.example.luan.adoptyourstreet.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.adapters.Shop_Adapter;
import com.example.luan.adoptyourstreet.models.ObjDemo;
import com.example.luan.adoptyourstreet.models.ShopObj;

import java.util.ArrayList;

/**
 * Created by Hai on 06/07/2016.
 */
public class ShopFragment extends Fragment {
    public ShopFragment() {
    }




    ArrayList<ShopObj> listEvent;
    Shop_Adapter eventAdapter;
    ListView listViewEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);


        listViewEvent = (ListView) rootView.findViewById(R.id.ListviewShop);
        dunyAdapter();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        listEvent = new ArrayList<>();
        listEvent.add(new ShopObj("Custom pin (Up to 10 people)",1));
        listEvent.add(new ShopObj("Custom pin (Up to 20 people)",2));
        listEvent.add(new ShopObj("Custom pin (Up to 100 people)",5));
        listEvent.add(new ShopObj("Custom pin (Up to 1000 people)",10));
        listEvent.add(new ShopObj("Custom pin (Up to 1000 people)",2));
        super.onCreate(savedInstanceState);
    }

    private void dunyAdapter() {


        eventAdapter = new Shop_Adapter(getActivity().getApplicationContext(), R.layout.item_shop, listEvent);
        listViewEvent.setAdapter(eventAdapter);
    }


}
