package com.example.luan.adoptyourstreet.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.activities.HomeActivity;
import com.example.luan.adoptyourstreet.activities.SignUpActivity;

/**
 * Created by Hai on 05/07/2016.
 */
public abstract class LoginFragment extends Fragment {
    public LoginFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_sign_up, container, false);
        Button btSignUp=(Button) rootView.findViewById(R.id.btnSigUp);
//        Button btLogin=(Button) rootView.findViewById(R.id.btnLogin);
//
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultSignUp();
            }
        });
//        btLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(rootView.getContext(), HomeActivity.class));
//            }
//        });
        return rootView;
    }

    public abstract void ResultSignUp();
}
