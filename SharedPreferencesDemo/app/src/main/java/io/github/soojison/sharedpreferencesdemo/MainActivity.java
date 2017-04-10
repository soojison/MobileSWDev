package io.github.soojison.sharedpreferencesdemo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_START_DATE = "KEY_START_DATE";
    private TextView tvStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);

        // use option + Shift + arrow keys to move the entire line up or down
        showLastUseTime();
    }

    @Override
    // this is when the user is actually interacting with the app
    protected void onResume() {
        super.onResume();
        saveUseTime();
    }

    // helper methods that save and load the timestamps
    private void saveUseTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_START_DATE,
                new Date(System.currentTimeMillis()).toString());
        editor.commit();
    }

    private void showLastUseTime() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        // it could be that nothing is under the key, so we must add a default value
        String lastStartDate = sp.getString(KEY_START_DATE, "This is the first time using the app");
        tvStartDate.setText(lastStartDate);
    }
}
