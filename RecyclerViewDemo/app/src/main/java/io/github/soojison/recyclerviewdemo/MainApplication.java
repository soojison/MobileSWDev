package io.github.soojison.recyclerviewdemo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {
    // this is a general class that is the main entry point of the application
    // accessible from all the activities, you can use a method that returns application object

    Realm realmTodo;

    @Override
    public void onCreate() { // not an activity class, so this oncreate is totally different
        super.onCreate(); // mandatory

        // init realm db
        Realm.init(this);
    }

    // create realm method
    // also implement migration strategies
    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmTodo = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmTodo.close();
    }

    public Realm getRealm() {
        return realmTodo;
    }

    // onTerminate method doesn't close the Realm DB
    // so don't think that you can use it... it only works on emulators
}
