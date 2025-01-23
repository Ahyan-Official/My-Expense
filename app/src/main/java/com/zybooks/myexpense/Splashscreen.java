package com.zybooks.myexpense;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent( Splashscreen.this,     MainActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }
        }, secondsDelayed * 3000);
    }
}
