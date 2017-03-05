package io.github.soojison.minesweeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import io.github.soojison.minesweeper.model.GameLogic;
import io.github.soojison.minesweeper.view.GridView;

public class MainActivity extends AppCompatActivity {

    private TextView tvData;
    private ImageButton imgBtnReset;
    public static final short EXPLORE = 1;
    public static final short FLAG = 2;
    public static short choice = 0;
    public boolean isTouchable = false;
    public boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final GridView gameView = (GridView) findViewById(R.id.gridView);


        tvData = (TextView) findViewById(R.id.gridInfo);

        imgBtnReset = (ImageButton) findViewById(R.id.imgBtnReset);
        Button btnExplore = (Button) findViewById(R.id.btnExplore);
        Button btnFlag = (Button) findViewById(R.id.btnFlag);
        GameLogic.getInstance().resetModel();

        imgBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });

        setMessage("Choose an action from below");
        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    choice = EXPLORE;
                    setMessage("Choose a tile to explore");
                    isTouchable = true;
                }
            }
        });

        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gameOver) {
                    choice = FLAG;
                    setMessage("Choose a tile to place the flag");
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
        setMessage("GAME OVER");
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.cons);
        imgBtnReset.setImageBitmap(bmp);
    }

    public void gameWon() {
        isTouchable = false;
        gameOver = true;
        setMessage("You've discovered all the mines!");
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.win);
        imgBtnReset.setImageBitmap(bmp);
    }

    public void reset() {
        isTouchable = false;
        gameOver = false;
        choice = 0;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pros);
        imgBtnReset.setImageBitmap(bmp);
    }
}
