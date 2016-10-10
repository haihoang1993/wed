package com.example.luan.adoptyourstreet.Util;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luan.adoptyourstreet.R;

/**
 * Created by Hai on 20/07/2016.
 */
public class ActionBar extends AppCompatActivity {
    Activity context;
    Toolbar toolbar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public ActionBar(Activity c, Toolbar toolbar) {
        this.toolbar = toolbar;
         context = (AppCompatActivity) c;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setTileActionBar(String t) {
        TextView tv = (TextView) context.findViewById(R.id.tvActionbar);
        tv.setText(t + "");
    }

    public void setIconActionBar(int imgDraw) {
        ImageView img = (ImageView) context.findViewById(R.id.imgIconBar);
        img.setImageResource(imgDraw);
    }

}
