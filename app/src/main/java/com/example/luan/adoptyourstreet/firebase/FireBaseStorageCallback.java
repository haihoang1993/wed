package com.example.luan.adoptyourstreet.firebase;

/**
 * Created by luan on 6/14/16.
 */
public interface FireBaseStorageCallback {

    void onLoadImageSuccess(byte[] bytes, String uid);

    void onLoadImageFail();
}
