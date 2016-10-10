package com.example.luan.adoptyourstreet.firebase;

import com.example.luan.adoptyourstreet.models.Event;
import com.example.luan.adoptyourstreet.models.Prayer;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import android.support.annotation.NonNull;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import com.google.android.gms.tasks.Task;

/**
 * Created by luan on 6/14/16.
 */
public class FireBaseStorageManager {

    private FirebaseStorage firebaseStorage;
    private StorageReference imagesRef;
    private StorageReference imagesEvebtRef;
    public static FireBaseStorageManager sharedInstance = new FireBaseStorageManager();

    FireBaseStorageManager() {
        firebaseStorage = FirebaseStorage.getInstance();

    }

    public void init() {
        imagesRef = firebaseStorage.getReference().child("images");
        imagesEvebtRef=firebaseStorage.getReference().child("imageEvents");
    }

    public void getImage(final String id, final FireBaseStorageCallback callback) {

        Log.i("FirbaseStorage", "get Image with id : " +id);

        StorageReference imageRef = imagesRef.child(id + ".png");

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(25 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                callback.onLoadImageSuccess(bytes, id);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                callback.onLoadImageFail();
                // Handle any errors
            }
        });
    }

    public void saveImagesEvent(final Event event,Bitmap bitmap){
        StorageReference imgaeRef=imagesEvebtRef.child(event.id+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imgaeRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                FireBaseDatabaseManager.sharedInstance.setImagesEvent(event);
            }
        });
    }

    public void saveImage(final Prayer prayer, Bitmap bitmap) {
        StorageReference imageRef = imagesRef.child(prayer.uid + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                prayer.hasImage = false;
                FireBaseDatabaseManager.sharedInstance.setPrayerHasImage(prayer, false);
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
}
