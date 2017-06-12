package com.aurora.elezov.myapplication;

import android.app.Application;

import com.vk.sdk.VKSdk;

/**
 * Created by USER on 28.02.2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VKSdk.initialize(this);

    }
}
