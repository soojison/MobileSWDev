package io.github.soojison.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.soojison.minesweeper.view.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gameView = (GridView) findViewById(R.id.gridView);

    }
}
