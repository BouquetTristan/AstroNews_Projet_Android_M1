package com.astronews.activities;

import android.app.AlertDialog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Toast;

import com.astronews.adapter.ListPlanetsAdapter;
import com.astronews.core.model.ssod.PlanetCollection;
import com.astronews.core.services.SsodService;


import dmax.dialog.SpotsDialog;
import rx.Observer;
import rx.Subscription;
import rx.android.concurrency.AndroidSchedulers;
import rx.concurrency.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Intent detail = new Intent(this, SystemUnityActivity.class);
        detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
