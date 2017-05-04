package io.github.soojison.fragmentbasicsdemo;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showMyFragment();
    }

    // you didn't even touch the main activity class!
    // touch event handled in fragment class, etc.

    // dynamic attachment
    public void showMyFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // start a transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // thing to replace, thing to put, the tag
        ft.replace(R.id.fragmentHolder, new MyFragment(), MyFragment.TAG);

        ft.commit();
    }
}
