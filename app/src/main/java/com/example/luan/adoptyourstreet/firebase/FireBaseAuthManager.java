package com.example.luan.adoptyourstreet.firebase;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by luan on 6/10/16.
 */

public class FireBaseAuthManager {

    private FirebaseAuth mAuth = null;
    private FirebaseUser user = null;
    private FirebaseAuth.AuthStateListener mAuthListener = null;
    public static FireBaseAuthManager sharedInstance = new FireBaseAuthManager();

    private String TAG = "FireBaseAuthManager";

    FireBaseAuthCallback delegate = null;
    public void init() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (delegate != null) {
                        delegate.onLoginSuccess(user);
                    }
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInAnonymously", task.getException());
                        }
                    }
                });
    }

    public void onStart() {
        Log.i("FireBaseAuth", "Add Login Listener");
        mAuth.addAuthStateListener(mAuthListener);
    }

    public Boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public String getUserUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public void onTerm() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setFireBaseAuthCallback(FireBaseAuthCallback delegate)
    {
        this.delegate = delegate;
    }

}
