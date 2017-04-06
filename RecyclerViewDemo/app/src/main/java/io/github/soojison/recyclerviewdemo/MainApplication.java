package io.github.soojison.recyclerviewdemo;

import android.app.Application;

import io.realm.Realm;

public class MainApplication extends Application {
    // this is a general class that is the main entry point of the application
    // accessible from all the activities, you can use a method that returns application object

    @Override
    public void onCreate() { // not an activity class, so this oncreate is totally different
        super.onCreate(); // mandatory

        // init realm db
        Realm.init(this);
    }

    // onTerminate method doesn't close the Realm DB
    // so don't think that you can use it... it only works on emulators
}
