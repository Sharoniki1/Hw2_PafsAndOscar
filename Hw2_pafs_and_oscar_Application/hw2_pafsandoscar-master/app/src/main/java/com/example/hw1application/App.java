package com.example.hw1application;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SPV3.init(this);
        MySignal.init(this);


    }
}
