package com.example.luan.adoptyourstreet.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.luan.adoptyourstreet.R;

public class SignInActivity extends AppCompatActivity {
    // action bar
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setActionBar();
        setTileActionBar("User Name");

        Button btSignUp=(Button) findViewById(R.id.btnSigUp);
        Button btLogin=(Button) findViewById(R.id.btnLogin);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
    }
    private void setActionBar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button bt=(Button)findViewById(R.id.btnCreateAppBar);
        bt.setVisibility(View.GONE);
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
    private void setTileActionBar(String t) {
        TextView tv = (TextView) findViewById(R.id.tvActionbar);
        tv.setText(t + "");
    }

    private void setIconActionBar(int imgDraw) {
        ImageView img = (ImageView) findViewById(R.id.imgIconBar);
        img.setImageResource(imgDraw);
    }
}
