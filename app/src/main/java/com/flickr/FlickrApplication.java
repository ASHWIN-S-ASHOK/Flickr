package com.flickr;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class FlickrApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Fresco.shutDown();
    }

}