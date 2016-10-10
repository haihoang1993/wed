package com.example.luan.adoptyourstreet.fragment;

import android.app.Fragment;
import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.luan.adoptyourstreet.R;
/**
 * Created by Hai on 04/07/2016.
 */
public class HomepageFragment extends Fragment  {
   public HomepageFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_homepage, container, false);

        return rootView;
    }
}
