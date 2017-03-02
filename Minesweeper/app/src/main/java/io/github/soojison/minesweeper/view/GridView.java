package io.github.soojison.minesweeper.view;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import io.github.soojison.minesweeper.MainActivity;
import io.github.soojison.minesweeper.R;
import io.github.soojison.minesweeper.model.GameLogic;

import static android.graphics.Color.rgb;

public class GridView extends View {

    private Paint paintBG;
    private Paint paintLine;
    private Paint paintTextR;
    private Paint paintTextG;
    private Paint paintTextB;
    private Paint paintTextDB;
    private Paint paintTextDR;
    private Paint paintDiscoveredBG;
    private Bitmap bitmapBomb;
    private Bitmap bitmapFlag;
    private Bitmap bitmapTile;

    private void setPaintObjAttrs() {
        paintBG.setColor(Color.GRAY);
        paintLine.setColor(Color.DKGRAY);
        paintLine.setStrokeWidth(5);
        paintTextR.setColor(rgb(211, 47, 47));
        paintTextG.setColor(rgb(56, 142, 60));
        paintTextB.setColor(rgb(25, 118, 210));
        paintTextDB.setColor(rgb(48, 63, 159));
        paintTextDR.setColor(rgb(183, 28, 28));
        paintTextR.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paintTextG.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paintTextB.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paintTextDB.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paintTextDR.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paintDiscoveredBG.setColor(Color.LTGRAY);
        paintBG.setStyle(Paint.Style.FILL);
        paintLine.setStyle(Paint.Style.STROKE);
        paintDiscoveredBG.setStyle(Paint.Style.FILL);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBG = new Paint();
        paintLine = new Paint();
        paintTextR = new Paint();
        paintTextG = new Paint();
        paintTextB = new Paint();
        paintTextDR = new Paint();
        paintTextDB = new Paint();
        paintDiscoveredBG = new Paint();
        bitmapBomb = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);
        bitmapTile = BitmapFactory.decodeResource(getResources(), R.drawable.tile);
        setPaintObjAttrs();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBG);
        drawProgress(canvas);

        drawGrid(canvas);

        ((MainActivity) getContext()).isTouchable = false;
        if(!((MainActivity) getContext()).gameOver) {
            ((MainActivity) getContext()).setMessage("Choose an action from below");
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        int screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        int smallerValue =
                screenWidth == 0 ?
                        screenHeight : screenHeight == 0 ?
                        screenWidth : screenWidth < screenHeight?
                        screenWidth : screenHeight;
        setMeasuredDimension(smallerValue, smallerValue);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmapBomb = Bitmap.createScaledBitmap(bitmapBomb, getWidth()/6, getHeight()/6, false);
        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth()/6, getHeight()/6, false);
        bitmapTile = Bitmap.createScaledBitmap(bitmapTile, getWidth()/5, getHeight()/5, false);

        paintTextR.setTextSize(getWidth()/7);
        paintTextG.setTextSize(getHeight()/7);
        paintTextB.setTextSize(getHeight()/7);
        paintTextDB.setTextSize(getHeight()/7);
        paintTextDR.setTextSize(getHeight()/7);

    }

    private void drawGrid(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);

        // four horizontal lines
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(0, i * getHeight() / 5, getWidth(), i * getHeight() / 5,
                    paintLine);
        }

        // four vertical lines
        for (int i = 1; i < 5; i++) {
            canvas.drawLine(i * getWidth() / 5, 0, i * getWidth() / 5, getHeight(),
                    paintLine);
        }
    }

    private Pair<String, Paint> drawHints(short hint) {
        Pair<String, Paint> ret;
        switch (hint) {
            case GameLogic.ONE:
                ret = new Pair<>("1", paintTextB);
                break;
            case GameLogic.TWO:
                ret = new Pair<>("2", paintTextG);
                break;
            case GameLogic.THREE:
                ret = new Pair<>("3", paintTextR);
                break;
            case GameLogic.FOUR:
                ret = new Pair<>("4", paintTextDB);
                break;
            case GameLogic.FIVE:
                ret = new Pair<>("5", paintTextDR);
                break;
            default:
                ret = new Pair<>("wtf", paintTextB);
                break;
        }
        return ret;
    }

    private void drawProgress(Canvas canvas) {
        int padding = getWidth()/20;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(GameLogic.getInstance().getModelField(i,j) == GameLogic.UNDISCOVERED) {
                    canvas.drawBitmap(bitmapTile, i*getWidth()/5, j*getHeight()/5, null);
                }
                if (GameLogic.getInstance().getModelField(i, j) == GameLogic.DISCOVERED) {
                        canvas.drawRect(i * getWidth() / 5, j * getHeight() / 5,
                                (i + 1) * getWidth() / 5, (j + 1) * getHeight() / 5,
                                paintDiscoveredBG);
                    if(GameLogic.getInstance().getHiddenModelField(i,j) > 0) {
                        Pair txt = drawHints(GameLogic.getInstance().getHiddenModelField(i,j));
                        canvas.drawText((String)txt.first, i*getWidth()/5 + padding, (j+1)*getHeight()/5 - padding, (Paint) txt.second);
                    }
                } else if (GameLogic.getInstance().getModelField(i, j) == GameLogic.BOMB) {
                    canvas.drawRect(i * getWidth() / 5, j * getHeight() / 5,
                            (i + 1) * getWidth() / 5, (j + 1) * getHeight() / 5,
                            paintDiscoveredBG);
                    canvas.drawBitmap(bitmapBomb, i * getWidth() / 5 + padding/3, j * getHeight() / 5 + padding/3, null);
                } else if (GameLogic.getInstance().getModelField(i,j) == GameLogic.BOMB_ORIGIN) { // only when game over
                    canvas.drawRect(i*getWidth()/5, j*getHeight()/5, (i+1) * getWidth()/5, (j+1) * getHeight()/5, paintTextR);
                    canvas.drawBitmap(bitmapBomb, i * getWidth() / 5 + padding/3, j * getHeight() / 5 + padding/3, null);
                } else if (GameLogic.getInstance().getModelField(i, j) == GameLogic.FLAG) {
                    canvas.drawBitmap(bitmapTile, i*getWidth()/5, j*getHeight()/5, null);
                    canvas.drawBitmap(bitmapFlag, i * getWidth() / 5 + padding/3, j * getHeight() / 5 + padding/3, null);
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = ((int)event.getX() / (getWidth()/5));
            int touchY = ((int)event.getY() / (getHeight()/5));


            if(((MainActivity) getContext()).isTouchable && !((MainActivity) getContext()).gameOver) {
                if( ((MainActivity) getContext()).getChoice() == MainActivity.EXPLORE ) { // if true, then we are exploring

                    if(GameLogic.getInstance().getModelField(touchX, touchY) == GameLogic.UNDISCOVERED) {
                        if(GameLogic.getInstance().getHiddenModelField(touchX, touchY) == GameLogic.BOMB) {
                            GameLogic.getInstance().setModelField(touchX, touchY, GameLogic.BOMB_ORIGIN);
                            gameOver();
                        } else {
                            GameLogic.getInstance().expandNearbyEmpty(touchX, touchY);
                            GameLogic.getInstance().setModelField(touchX, touchY, GameLogic.DISCOVERED);
                        }
                    }
                } else if ( ((MainActivity) getContext()).getChoice() != MainActivity.EXPLORE ){ // then we are placing the flag
                    if(GameLogic.getInstance().getModelField(touchX, touchY) == GameLogic.UNDISCOVERED) {
                        GameLogic.getInstance().setModelField(touchX, touchY, GameLogic.FLAG);
                    }
                } else {
                    Toast.makeText(getContext(), "pls action pls", Toast.LENGTH_SHORT).show();
                }
                invalidate();
                MainActivity.choice = 0; // reset choice
            } else if(!((MainActivity) getContext()).gameOver) {
                Toast.makeText(getContext(),
                        "Please choose an action", Toast.LENGTH_SHORT).show();
            } else if(GameLogic.getInstance().gameWon()) {
                Toast.makeText(getContext(), "u won", Toast.LENGTH_SHORT).show();
            }
        }

        return true;
    }

    public void resetGame() {
        GameLogic.getInstance().resetModel();
        ((MainActivity) getContext()).reset();
        invalidate();
    }

    public void gameOver() {
        GameLogic.getInstance().gameOver();
        ((MainActivity) getContext()).gameOver();
        invalidate();
    }
}
