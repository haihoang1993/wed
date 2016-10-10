package com.example.luan.adoptyourstreet.firebase;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by luan on 6/13/16.
 */
public interface FireBaseAuthCallback {
    void onLoginSuccess(FirebaseUser user);
}
