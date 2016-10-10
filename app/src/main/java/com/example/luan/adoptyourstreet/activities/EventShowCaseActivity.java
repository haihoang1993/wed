package com.example.luan.adoptyourstreet.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.luan.adoptyourstreet.R;
import com.example.luan.adoptyourstreet.adapters.Comment_Adapter;
import com.example.luan.adoptyourstreet.adapters.Shop_Adapter;
import com.example.luan.adoptyourstreet.models.ObjDemo;

import java.util.ArrayList;

public class EventShowCaseActivity extends AppCompatActivity {
    private TextView tvNameEvent;
    // action bar
    Toolbar toolbar;
   ListView lvComment;
    ArrayList<ObjDemo> listEvent;
    Comment_Adapter comment_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show_case);
        setActionBar();
        dunyAdapter();
        setTab();
    }
    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void dunyAdapter() {
        lvComment=(ListView) findViewById(R.id.lvComment);
        listEvent = new ArrayList<>();
        listEvent.add(new ObjDemo(12, "Hai"));
        listEvent.add(new ObjDemo(22, "Hai"));
        listEvent.add(new ObjDemo(221, "Hai"));
        listEvent.add(new ObjDemo(12, "Hai"));
        listEvent.add(new ObjDemo(22, "Hai"));
        listEvent.add(new ObjDemo(221, "Hai"));
        listEvent.add(new ObjDemo(12, "Hai"));
        listEvent.add(new ObjDemo(22, "Hai"));
        listEvent.add(new ObjDemo(221, "Hai"));
        comment_adapter = new Comment_Adapter(getApplicationContext(), R.layout.item_shop, listEvent);
        lvComment.setAdapter(comment_adapter);
        lvComment.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getParent().requestDisallowInterceptTouchEvent(false);

                }
                return false;
            }
        });
        FrameLayout f=(FrameLayout) findViewById(R.id.frameLv);
        //int height=f.get.height;
//        height *= listEvent.size();
////        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams();
////        f.setLayoutParams(layoutParams);
//        Toast.makeText(getApplicationContext(),height+"",Toast.LENGTH_LONG).show();
//        lvComment.set
    }
    private void setView() {
        tvNameEvent = (TextView) findViewById(R.id.tvNameEventShowcase);
        Spanned htmlAsSpanned = Html.fromHtml("<b> Funkoi </b> with <b>name</b> and <b> 20 Others</b>");
        tvNameEvent.setText(htmlAsSpanned);
    }

     private void setClickContentViewTab(int i){
         FrameLayout f1=(FrameLayout) findViewById(R.id.pImage);
         f1.setVisibility(View.GONE);
         FrameLayout f2=(FrameLayout) findViewById(R.id.pMapg);
         f2.setVisibility(View.GONE);
         FrameLayout f3=(FrameLayout) findViewById(R.id.pCoutdown);
         f3.setVisibility(View.GONE);
         if(i==1){
             f1.setVisibility(View.VISIBLE);
         }
         if(i==2){
             f2.setVisibility(View.VISIBLE);
         }
         if(i==3){
             f3.setVisibility(View.VISIBLE);
         }
     }

    private void setClickTileViewTab(int i){
        FrameLayout f1=(FrameLayout) findViewById(R.id.tab1Event);
        f1.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        FrameLayout f2=(FrameLayout) findViewById(R.id.tab2Event);
        f2.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        FrameLayout f3=(FrameLayout) findViewById(R.id.tab3Event);
        f3.setBackgroundColor(getResources().getColor(R.color.ColorEvent));
        TextView t1=(TextView) findViewById(R.id.icTitTab1);
        TextView t2=(TextView) findViewById(R.id.icTitTab2);
        TextView t3=(TextView) findViewById(R.id.icTitTab3);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        t3.setVisibility(View.VISIBLE);
        if(i==1){
            f1.setBackgroundColor(getResources().getColor(R.color.white));
            t1.setVisibility(View.GONE);
        }
        if(i==2){
            f2.setBackgroundColor(getResources().getColor(R.color.white));
            t2.setVisibility(View.GONE);
        }
        if(i==3){
            f3.setBackgroundColor(getResources().getColor(R.color.white));
            t3.setVisibility(View.GONE);
        }

    }

    private void setTab(){
        FrameLayout f1=(FrameLayout) findViewById(R.id.tab1Event);
        FrameLayout f2=(FrameLayout) findViewById(R.id.tab2Event);
        FrameLayout f3=(FrameLayout) findViewById(R.id.tab3Event);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setClickTileViewTab(1);
                setClickContentViewTab(1);
            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickTileViewTab(2);
                setClickContentViewTab(2);
            }
        });
        f3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickTileViewTab(3);
                setClickContentViewTab(3);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
