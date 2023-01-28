package com.example.hw1application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

public class LeaderBoardActivity extends AppCompatActivity {

    public static final String KEY_SCORE = "KEY_SCORE";
    private Score tempScore;
    private FragmentList fragmentList;
    private FragmentMap fragmentMap;
    private MaterialButton leaderboard_BTN_menu;
    FusedLocationProviderClient fusedLocationProviderClient;

    CallBack_ScoreProtocol callBack_scoreProtocol = new CallBack_ScoreProtocol() {
        @Override
        public void score(Score score) {
            fragmentMap.zoom(score.getLatitude(), score.getLongitude());
        }
    };

    CallBack_TimingProtocol callBackTimingProtocol = new CallBack_TimingProtocol() {
        @Override
        public void ready() {
            fragmentList.read();
            if(tempScore != null) {
                tempScore = fragmentList.getLeaderBoard().addScore(tempScore);
                if(tempScore != null) {
                    fragmentList.write();
                }
            }
            fragmentList.refreshUI();
            fragmentMap.refreshUI(fragmentList.getLeaderBoard().getScores());
        }
    };

    @Override
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        leaderboard_BTN_menu = findViewById(R.id.leaderboard_BTN_menu);

        leaderboard_BTN_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuButtonClicked();
            }
        });
        fragmentList = new FragmentList();
        fragmentMap = new FragmentMap();

        Intent previousIntent = getIntent();
        if(previousIntent.getExtras() != null) {
            tempScore = new Score();
            tempScore.setScore(previousIntent.getExtras().getInt("KEY_SCORE"));
        }

        fragmentList.setCallBack_scoreProtocol(callBack_scoreProtocol);
        fragmentList.setCallBack_timingProtocol(callBackTimingProtocol);

        if(tempScore != null) {
            fusedLocationProviderClient = LocationServices.
                    getFusedLocationProviderClient(this);
            fetchLastLocation();
        }

        getSupportFragmentManager()
                .beginTransaction().replace(R.id.leaderboard_FLAY_map, fragmentMap)
                .commit();
        getSupportFragmentManager().beginTransaction().add(R.id.leaderboard_FLAY_list, fragmentList).commit();
    }

    @SuppressLint("MissingPermission")
    public void fetchLastLocation() {

        if (ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 123);

            return;

        } else {

            if (isLocationEnabled()) {

                fusedLocationProviderClient.
                        getLastLocation().
                        addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            if(tempScore != null) {
                                tempScore.
                                        setLatitude(location.getLatitude()).
                                        setLongitude(location.getLongitude());
                            }
                        }
                    }
                });

            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(500)
                .setMaxUpdateDelayMillis(1000)
                .build();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location lastLocation = locationResult.getLastLocation();
            tempScore.
                    setLatitude(lastLocation.getLatitude()).
                    setLongitude(lastLocation.getLongitude());
        }
    };

    private void menuButtonClicked() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}