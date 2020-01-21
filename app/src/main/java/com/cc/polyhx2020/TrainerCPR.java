package com.cc.polyhx2020;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class TrainerCPR extends AppCompatActivity implements SensorEventListener {

    Sensor accelerometer;
    SensorManager sm;
    RelativeLayout RL;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton backBtn = findViewById(R.id.backFromCPRimageButton);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);

        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        RL = findViewById(R.id.CPRlayout);

        mp = MediaPlayer.create(this, R.raw.beep);

        new CountDownTimer(60100, 800) {

            @Override
            public void onTick(long millisUntilFinished) {
                mp.start();
            }

            @Override
            public void onFinish() {

            }

        }.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        double theta = Math.acos(event.values[2]/9.8);
        double sinTheta = Math.tan(theta);
        double longueur = 15;

        Log.d("" + event.values[2], "" + longueur*sinTheta);

        if (longueur * Math.sin(theta) >= 5) {
            RL.setBackgroundColor(Color.parseColor("#00ff00"));
        } else if( longueur*sinTheta > 1){
            RL.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }

    public void backFromCPR(View view) {
        startActivity(new Intent(TrainerCPR.this, MainActivity.class));
    }
}
