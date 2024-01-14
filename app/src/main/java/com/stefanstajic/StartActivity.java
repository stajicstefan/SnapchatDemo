package com.stefanstajic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.stefanstajic.home.HomeActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler(Looper.getMainLooper()).postDelayed(this::launchHome, 2000);
    }

    private void launchHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}