package io.github.soojison.minesweeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.github.soojison.minesweeper.model.GameLogic;
import io.github.soojison.minesweeper.view.GridView;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;
    private ImageButton imgBtnReset;
    public static short choice = 0;
    public static final short EXPLORE = 1;
    public static final short FLAG = 2;
    public boolean isTouchable = false;
    public boolean gameOver = false;
    public LinearLayout layoutRoot;
    private Chronometer chronos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView gameView = (GridView) findViewById(R.id.gridView);

        tvData = (TextView) findViewById(R.id.gridInfo);
        chronos = new Chronometer(this);

        imgBtnReset = (ImageButton) findViewById(R.id.imgBtnReset);
        Button btnExplore = (Button) findViewById(R.id.btnExplore);
        Button btnFlag = (Button) findViewById(R.id.btnFlag);
        layoutRoot = (LinearLayout) findViewById(R.id.activity_main);

        GameLogic.getInstance().resetModel();

        imgBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });

        setMessage(getString(R.string.choose_action_below));
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    choice = EXPLORE;
                    setMessage(getString(R.string.choose_tile_explore));
                    isTouchable = true;
                }
            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    choice = FLAG;
                    setMessage(getString(R.string.choose_tile_flag));
                    isTouchable = true;
                }
            }
        });


    }

    public void setMessage(String text) {
        tvData.setText(text);
    }


    public short getChoice() {
        return choice;
    }

    public void gameOver() {
        isTouchable = false;
        gameOver = true;
        chronos.stop();
        setMessage(getString(R.string.game_over));
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cons);
        imgBtnReset.setImageBitmap(bmp);

    }

    public void gameWon() {
        isTouchable = false;
        gameOver = true;
        chronos.stop();
        long millis = SystemClock.elapsedRealtime() - chronos.getBase();
        double secs = TimeUnit.MILLISECONDS.toSeconds(millis);
        setMessage(getString(R.string.you_win, secs));
        tvData.setTextColor(Color.RED);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.win);
        imgBtnReset.setImageBitmap(bmp);

    }

    public void reset() {
        isTouchable = false;
        gameOver = false;
        choice = 0;
        tvData.setTextColor(Color.BLACK);
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pros);
        imgBtnReset.setImageBitmap(bmp);
        chronos.setBase(SystemClock.elapsedRealtime());
        chronos.start();
    }
}
