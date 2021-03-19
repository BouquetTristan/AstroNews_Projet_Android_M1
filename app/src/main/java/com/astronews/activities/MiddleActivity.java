package com.astronews.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MiddleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent call = new Intent(MiddleActivity.this, SystemUnityActivity.class);
            startActivity(call);
    }
}
