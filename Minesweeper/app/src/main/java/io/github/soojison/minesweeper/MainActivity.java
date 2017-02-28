package io.github.soojison.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.soojison.minesweeper.view.GridView;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;
    private boolean isExplore; // explore if true, flag if false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gameView = (GridView) findViewById(R.id.gridView);
        tvData = (TextView) findViewById(R.id.gridInfo);

        Button btnReset = (Button) findViewById(R.id.btnReset);
        Button btnExplore = (Button) findViewById(R.id.btnExplore);
        Button btnFlag = (Button) findViewById(R.id.btnFlag);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });

        setMessage("Choose an action from below");
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExplore = true;
                setMessage("Choose a tile to explore");
            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isExplore = false;
                setMessage("Choose a tile to place the flag");
            }
        });


    }

    public void setMessage(String text) {
        tvData.setText(text);
    }

    public boolean getChoice() {
        return isExplore;
    }
}
