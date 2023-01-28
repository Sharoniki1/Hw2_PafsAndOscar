package com.example.hw1application;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepDetector {

    public interface CallBack_Steps {
        void oneStep(int indication);
    }
    private SensorManager mSensorManager;
    private Sensor sensor;

    long timeStamp;

    private CallBack_Steps callBack_steps;

    public StepDetector(Context context, CallBack_Steps _callBack_steps) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.callBack_steps = _callBack_steps;
    }
    public void start() {
        mSensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        mSensorManager.unregisterListener(sensorEventListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];

            calculateStep(x);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void calculateStep(float x) {
        if (x > 4.0) {
            if (System.currentTimeMillis() - timeStamp > 500) {
                timeStamp = System.currentTimeMillis();
                if (callBack_steps != null) {
                    callBack_steps.oneStep(-1);
                }
            }
        }

        if (x < -4.0) {
            if (System.currentTimeMillis() - timeStamp > 500) {
                timeStamp = System.currentTimeMillis();
                if (callBack_steps != null) {
                    callBack_steps.oneStep(1);
                }
            }
        }
    }
}


