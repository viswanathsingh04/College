package com.testingstudio33.common;

import android.app.Application;

public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new AppLifecycleTracker());
    }
}
