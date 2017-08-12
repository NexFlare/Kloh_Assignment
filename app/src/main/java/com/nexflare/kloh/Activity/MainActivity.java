package com.nexflare.kloh.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nexflare.kloh.API.KlohAPI;
import com.nexflare.kloh.Adapter.EventListAdapter;
import com.nexflare.kloh.Model.EventResponse;
import com.nexflare.kloh.Model.LocationModel;
import com.nexflare.kloh.Model.PostRequest;
import com.nexflare.kloh.Model.Result;
import com.nexflare.kloh.R;

import java.net.InetAddress;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_CODE = 28201;
    private static final int REQUEST_LOCATION = 28099;
    Double latitude=null,longitude=null;
    GoogleApiClient mGoogleApiClient;
    RecyclerView rvEventList;
    TextView tvEventAvailable;
    EventListAdapter eventAdapter;
    LocationRequest request;
    RelativeLayout rlError;
    Retrofit retrofit;
    Button btnRefresh;
    int isResultAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("TAGGER", "onCreate: ");
        isResultAvailable=0;
        btnRefresh= (Button) findViewById(R.id.btnRefresh);
        rlError= (RelativeLayout) findViewById(R.id.rlError);
        rlError.setVisibility(View.INVISIBLE);
        rvEventList= (RecyclerView) findViewById(R.id.rvEventList);
        tvEventAvailable= (TextView) findViewById(R.id.tvEventAvailable);
        tvEventAvailable.setVisibility(View.INVISIBLE);
        rvEventList.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter=new EventListAdapter(this,new ArrayList<Result>());
        rvEventList.setAdapter(eventAdapter);
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build();
        checkLocationPermission();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kloh.in//kloh/external/v1/activity/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void getAllEvents() {
        Log.d("TAGGER", "getAllEvents: ");
        if(latitude!=null&&longitude!=null){
            final ProgressDialog dialog=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Getting events near you");
            dialog.setCancelable(false);
            dialog.show();
            Log.d("TAGGER", "getAllEvents: INSIDE");
            PostRequest postRequest=new PostRequest(new LocationModel(12.926031,77.676246));
            Log.d("TAGGER", "getAllEvents: "+postRequest);
            KlohAPI api=retrofit.create(KlohAPI.class);
            api.getAllEvents(postRequest).enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, retrofit2.Response<EventResponse> response) {
                    dialog.dismiss();
                    isResultAvailable=1;
                    rlError.setVisibility(View.INVISIBLE);
                    int size=response.body().getResponse().getResults().size();
                    Log.d("TAGGER", "onResponse: "+response.body());
                    if(size==0){
                        tvEventAvailable.setVisibility(View.VISIBLE);
                    }
                    else{
                        eventAdapter.updateArrayList(response.body().getResponse().getResults());
                    }
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {
                    Log.d("TAGGER", "onFailure: ");
                    dialog.dismiss();
                    if(isResultAvailable==0)
                        rlError.setVisibility(View.VISIBLE);
                    btnRefresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isLocationEnabled();
                            getUpdatedLocation();
                        }
                    });
                    if(isInternetAvailable())
                        Toast.makeText(MainActivity.this, "Some error occurred", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void checkLocationPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }
        else
        {
            isLocationEnabled();
        }
    }

    private void isLocationEnabled() {
        Log.d("TAGGER", "isLocationEnabled: ");
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        //getUpdatedLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("TAGGER", "onConnected: ");

        request = LocationRequest.create()
                .setInterval(120000)
                .setFastestInterval(600000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        isLocationEnabled();

        getUpdatedLocation();
    }

    private void getUpdatedLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                getAllEvents();
                /*Toast.makeText(MainActivity.this, String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, String.valueOf(location.getLongitude()), Toast.LENGTH_SHORT).show();*/

            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE){

            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Log.d("TAGGER", "onRequestPermissionsResult: ");
                isLocationEnabled();
            }
            else
                finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAGGER", "onStart: ");
        tvEventAvailable.setVisibility(View.INVISIBLE);
        mGoogleApiClient.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    default: {
                        getUpdatedLocation();
                        break;
                    }
                }
                break;
        }

    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
