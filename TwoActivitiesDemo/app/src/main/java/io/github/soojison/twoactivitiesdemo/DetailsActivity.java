package io.github.soojison.twoactivitiesdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView tvData = (TextView) findViewById(R.id.tvData);

        // how do we know who started this activity? i.e. how can i get the intent?
        // student: probably use something like getIntent()?
        // pekler: thanks you've ruined my joke. -1 point what's your name?

        // make sure there is an intent with this key first
        if(getIntent().getExtras().containsKey(MainActivity.KEY_DATA)) {
            tvData.setText(getIntent().getStringExtra(MainActivity.KEY_DATA));
        }
    }
}
