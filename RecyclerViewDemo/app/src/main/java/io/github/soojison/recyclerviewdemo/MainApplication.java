package io.github.soojison.recyclerviewdemo;

import android.app.Application;

import io.realm.Realm;

public class MainApplication extends Application {

    @Override
    public void onCreate() { // not an activity class, so this oncreate is totally different
        super.onCreate(); // mandatory

        // init realm db
        Realm.init(this);
    }

    // onTerminate method doesn't close the Realm DB
    // so don't think that you can use it... it only works on emulators
}
