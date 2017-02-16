package hu.ait.aitlifecycledemo;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // extracted constants -- sooo important!!
    public static final String TAG_LIFE = "TAG_LIFE";
    int score = 27;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // always use super implementations!
        setContentView(R.layout.activity_main);

        Log.d(TAG_LIFE, "onCreate called");

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt("KEY_SCORE");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG_LIFE, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG_LIFE, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG_LIFE, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG_LIFE, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // not specified when the thing destroyed, so if you have to stop a thread,
        // do it in the onStop method
        Log.d(TAG_LIFE, "onDestroy called");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("KEY_SCORE", score);
    }

    // back button : onPause, onStop, onDestroy
    // home button : onPause, onStop -> onStart, onResume
    // square button : onPause, onStop -> onStart, onResume
    // incoming phone call : nothing happens, but if you answer it, you get onPause
    //                       stop call, you get onResume
    //                       (during phone call, the app is still is "visible,"
    //                              so you gotta put relevant code in onPause)
    // rotate the screen : pause, stop, destroy, create, start, resume
    //                     => whole activity is destroyed and a new one is created
    //                     pekeler: "you don't seem too afraid about this situation but you should be"
    //                     so you can use onSaveInstanceState method to save important values and states
    //                      or just force orientation mode

}
