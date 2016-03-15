package com.inapp.firebasechat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Inappian on 3/15/2016.
 */
public class FBCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
