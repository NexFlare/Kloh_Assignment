package com.nexflare.kloh.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nexflare.kloh.API.KlohAPI;
import com.nexflare.kloh.Model.EventDetailResponse;
import com.nexflare.kloh.Model.Result;
import com.nexflare.kloh.R;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventDetailActivity extends AppCompatActivity {
    public static final int MONTH=1;
    public static final int DATE=2;
    String activityId;
    ImageView ivEventDetailPhoto,ivEventDetailLogo;
    TextView tvEventDetailName,tvEventOrganizer,tvEventDetailDate,
            tvEventDetailMonth,tvEventTiming,tvEventDetailDescription,
            tvEventDetailLocation,tvEventDetailAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        activityId=getIntent().getStringExtra("activityId");
        ivEventDetailLogo= (ImageView) findViewById(R.id.ivEventDetailLogo);
        ivEventDetailPhoto= (ImageView) findViewById(R.id.ivEventDetailPhoto);
        tvEventDetailDate= (TextView) findViewById(R.id.tvEventDetailDate);
        tvEventDetailMonth= (TextView) findViewById(R.id.tvEventDetailMonth);
        tvEventDetailName= (TextView) findViewById(R.id.tvEventDetailName);
        tvEventOrganizer= (TextView) findViewById(R.id.tvEventOrganizer);
        tvEventTiming= (TextView) findViewById(R.id.tvEventTiming);
        tvEventDetailLocation= (TextView) findViewById(R.id.tvEventDetailLocation);
        tvEventDetailAddress= (TextView) findViewById(R.id.tvEventDetailAddress);
        tvEventDetailDescription= (TextView) findViewById(R.id.tvEventDetailDescription);
        //Toast.makeText(this, activityId, Toast.LENGTH_SHORT).show();
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.kloh.in//kloh/external/v1/activity/")
                .build();
        KlohAPI api=retrofit.create(KlohAPI.class);
        api.getEventDetail(activityId).enqueue(new Callback<EventDetailResponse>() {
            @Override
            public void onResponse(Call<EventDetailResponse> call, Response<EventDetailResponse> response) {
                Log.d("RESPONSEB", "onResponse: "+response.body());
                Result result=response.body().getResponse();
                Picasso.with(EventDetailActivity.this).load(result.getImageUrl()).fit().into(ivEventDetailPhoto);
                Picasso.with(EventDetailActivity.this).load(result.getOwnerProfileImageUrl()).fit().into(ivEventDetailLogo);
                tvEventDetailDescription.setText(result.getDescription());
                tvEventTiming.setText(result.getActivityTime().getActivityDateStringV1());
                tvEventDetailDate.setText(result.getActivityTime().getTimeTuples().get(DATE));
                tvEventDetailMonth.setText(result.getActivityTime().getTimeTuples().get(MONTH));
                tvEventOrganizer.setText("by "+result.getOwnerName());
                tvEventDetailName.setText(result.getTitle());
                tvEventDetailAddress.setText(result.getLocation().getAddress());
                tvEventDetailLocation.setText(result.getLocation().getName());
            }

            @Override
            public void onFailure(Call<EventDetailResponse> call, Throwable t) {
                Toast.makeText(EventDetailActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
