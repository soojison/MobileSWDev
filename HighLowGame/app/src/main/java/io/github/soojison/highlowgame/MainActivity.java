package io.github.soojison.highlowgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        Button btnHelp = (Button) findViewById(R.id.btnHelp);
        Button btnAbout = (Button) findViewById(R.id.btnHelp);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the game activity
                startActivity(new Intent().setClass(MainActivity.this, GameActivity.class));
            }
        });
    }
}
