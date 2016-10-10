package com.example.luan.adoptyourstreet.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luan.adoptyourstreet.R;

/**
 * Created by Hai on 05/07/2016.
 */
public class PinPageFragment extends Fragment {
    public PinPageFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pinpage, container, false);

        return rootView;
    }
}
