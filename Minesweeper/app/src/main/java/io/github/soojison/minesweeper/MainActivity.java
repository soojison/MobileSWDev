package io.github.soojison.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;

import io.github.soojison.minesweeper.view.GridView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gameView = (GridView) findViewById(R.id.gridView);

        Button btnReset = (Button) findViewById(R.id.btnReset);
        Button btnExplore = (Button) findViewById(R.id.btnExplore);
        Button btnFlag = (Button) findViewById(R.id.btnFlag);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });
    }
}
