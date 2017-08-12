package com.nexflare.kloh.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.nexflare.kloh.R;

public class EventDetailActivity extends AppCompatActivity {
    String activityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        activityId=getIntent().getStringExtra("activityId");
        Toast.makeText(this, activityId, Toast.LENGTH_SHORT).show();
    }
}
