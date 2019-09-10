package com.kh_sof_dev.admin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kh_sof_dev.admin.R;


public class LogoAct extends AppCompatActivity {

    private static int SPLASH_TIME = 3000;
    private TextView logo_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_a_logo);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LogoAct.this,login_admin.class));
finish();
            }
        }, SPLASH_TIME);

    }
}
