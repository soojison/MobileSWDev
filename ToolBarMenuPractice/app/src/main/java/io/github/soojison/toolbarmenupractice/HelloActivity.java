package io.github.soojison.toolbarmenupractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class HelloActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvText = (TextView) findViewById(R.id.tvText);
        if(getIntent().getExtras().containsKey(MainActivity.KEY_ETDATA)) {
            tvText.setText(getIntent().getStringExtra(MainActivity.KEY_ETDATA));
        }

    }
}
