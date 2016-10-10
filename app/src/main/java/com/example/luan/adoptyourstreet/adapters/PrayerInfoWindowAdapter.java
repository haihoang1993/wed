package com.example.luan.adoptyourstreet.adapters;

import com.example.luan.adoptyourstreet.R;

import com.example.luan.adoptyourstreet.firebase.FireBaseStorageCallback;
import com.example.luan.adoptyourstreet.firebase.FireBaseStorageManager;
import com.example.luan.adoptyourstreet.models.Prayer;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;


/**
 * Created by luan on 6/13/16.
 */


public abstract class PrayerInfoWindowAdapter implements InfoWindowAdapter,
                                                FireBaseStorageCallback {

    private final View mWindow;
    private final View mContents;
    private ImageView imageView;
    private Map<Marker, Prayer> showingMarkers;
    private Marker marker;

    public PrayerInfoWindowAdapter(Activity activity, Map<Marker, Prayer> prayers) {
        mWindow = activity.getLayoutInflater().inflate(R.layout.prayer_info_window, null);
        mContents = activity.getLayoutInflater().inflate(R.layout.prayer_info_content, null);
        showingMarkers = prayers;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        this.marker = marker;
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        this.marker = marker;
        return mContents;
    }

    private void render(Marker marker, View view) {

        Prayer prayer = showingMarkers.get(marker);
        if (prayer.hasImage) {
            Boolean skipLoading = false;
            if (this.marker != null) {
                Prayer oldPrayer = showingMarkers.get(this.marker);
                if (oldPrayer != prayer) {

                } else {
                    skipLoading = true;
                }
            }
            if (!skipLoading) {
                FireBaseStorageManager.sharedInstance.getImage(prayer.uid, this);
            }

        } else {

            imageView = null;
        }

        String title = marker.getTitle();
        Log.d("Get title:",title);
        renderViewPin(title,prayer);

    }
    public abstract void renderViewPin(String tit,Prayer prayer);
    @Override
    public void onLoadImageSuccess(byte[] bytes, String uid) {
        if (imageView != null && marker != null) {
            Prayer prayer = showingMarkers.get(marker);
            if (prayer.uid == uid && marker.isInfoWindowShown()) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }

    @Override
    public void onLoadImageFail() {

    }
}
