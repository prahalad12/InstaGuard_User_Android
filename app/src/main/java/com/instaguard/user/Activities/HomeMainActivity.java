package com.instaguard.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.instaguard.user.R;

public class HomeMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        findViewById(R.id.ll_one).setOnClickListener(view -> {
            HomeMainActivity.this.startActivity(new Intent(HomeMainActivity.this.getApplicationContext(),MainActivity.class));
            HomeMainActivity.this.finish();
        });
        findViewById(R.id.ll_two).setOnClickListener(view -> {
            HomeMainActivity.this.startActivity(new Intent(HomeMainActivity.this.getApplicationContext(),MainActivity.class));
            HomeMainActivity.this.finish();
        });

    }
}