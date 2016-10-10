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
public class SignUpResultFragment extends Fragment {
    public SignUpResultFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_sign_up_result, container, false);
//        Button btSignUp=(Button) rootView.findViewById(R.id.btnSigUp);
//        Button btLogin=(Button) rootView.findViewById(R.id.btnLogin);
//
//        btSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(rootView.getContext(),SignUpActivity.class));
//            }
//        });
//        btLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(rootView.getContext(), HomeActivity.class));
//            }
//        });
        return rootView;
    }
}
