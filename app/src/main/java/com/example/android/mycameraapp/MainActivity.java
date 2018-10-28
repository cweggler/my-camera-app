package com.example.android.mycameraapp;

import android.hardware.camera2.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction();
                .replace(R.id. , Fragment.newInstance());
                .commit();
        }
    }

}
