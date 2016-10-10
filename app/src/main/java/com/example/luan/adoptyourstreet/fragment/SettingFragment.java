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
public class SettingFragment extends Fragment {
    public SettingFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        return rootView;
    }
}
