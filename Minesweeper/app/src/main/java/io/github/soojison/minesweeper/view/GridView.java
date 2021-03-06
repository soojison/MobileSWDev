package io.github.soojison.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import io.github.soojison.minesweeper.MainActivity;
import io.github.soojison.minesweeper.R;
import io.github.soojison.minesweeper.model.Field;
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
        paintDiscoveredBG.setColor(rgb(189,189,189));
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

        ((MainActivity) getContext()).isTouchable = false;
        if(!((MainActivity) getContext()).gameOver) {
            ((MainActivity) getContext()).setMessage(getResources().getString(R.string.choose_action_below));
            drawProgress(canvas);
        } else {
            drawGameOver(canvas);
        }
        drawGrid(canvas);

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

        paintTextR.setTextSize(getWidth()/5);
        paintTextG.setTextSize(getHeight()/5);
        paintTextB.setTextSize(getHeight()/5);
        paintTextDB.setTextSize(getHeight()/5);
        paintTextDR.setTextSize(getHeight()/5);

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

    private Pair<String, Paint> getHintAttrs(int hint) {
        Pair<String, Paint> ret;
        switch (hint) {
            case 1:
                ret = new Pair<>("1", paintTextB);
                break;
            case 2:
                ret = new Pair<>("2", paintTextG);
                break;
            case 3:
                ret = new Pair<>("3", paintTextR);
                break;
            case 4:
                ret = new Pair<>("4", paintTextDB);
                break;
            case 5:
                ret = new Pair<>("5", paintTextDR);
                break;
            default:
                ret = new Pair<>("unknown value", paintTextB); // Gives me error w/o the default case
                break;
        }
        return ret;
    }

    private void drawUndiscoveredTile(Canvas canvas, int i, int j, int tileSize) {
        canvas.drawBitmap(bitmapTile, i * tileSize, j * tileSize, null);
    }

    private void drawFlagTile(Canvas canvas, int i, int j, int tileSize) {
        int padding = getWidth()/60;
        drawUndiscoveredTile(canvas, i, j, tileSize);
        canvas.drawBitmap(bitmapFlag, i * tileSize + padding, j * tileSize + padding, null);
    }

    private void drawDiscoveredTile(Canvas canvas, int i, int j, int tileSize) {
        canvas.drawRect(i* tileSize, j* tileSize,
                (i+1) * tileSize, (j+1) * tileSize,
                paintDiscoveredBG);
    }

    private void drawHints(Canvas canvas, int i, int j, int tileSize) {
        int padding = getWidth()/20;
        Pair txt = getHintAttrs(GameLogic.getInstance().getFieldAt(i, j).getBombsNearBy());
        canvas.drawText((String) txt.first, i * tileSize + padding,
                (j+1) * tileSize - padding/2, (Paint) txt.second);
    }

    private void drawBomb(Canvas canvas, int i, int j, int tileSize) {
        int padding = getWidth()/60;
        drawDiscoveredTile(canvas, i, j, tileSize);
        canvas.drawBitmap(bitmapBomb, i * tileSize + padding,
                j * tileSize + padding, null);
    }

    private void drawBombOrigin(Canvas canvas, int i, int j, int tileSize) {
        int padding = getWidth()/60;
        canvas.drawRect(i * tileSize, j * tileSize,
                (i+1) * tileSize, (j+1) * tileSize, paintTextR);
        canvas.drawBitmap(bitmapBomb, i * tileSize + padding,
                j * tileSize + padding, null);
    }

    private void drawProgress(Canvas canvas) {
        int tileSize = getWidth()/GameLogic.GRID_SIZE; // our game will always have the condition width = height
        Field curModelField;
        for (int i = 0; i < GameLogic.GRID_SIZE; i++) {
            for (int j = 0; j < GameLogic.GRID_SIZE; j++) {
                curModelField = GameLogic.getInstance().getFieldAt(i,j);
                if (!curModelField.isDiscovered()) {
                    drawUndiscoveredTile(canvas, i, j, tileSize);
                } else if(curModelField.isFlagged()) {
                    drawFlagTile(canvas, i, j, tileSize);
                } else if(curModelField.isDiscovered()) {
                    drawDiscoveredTile(canvas, i, j, tileSize);
                    if(curModelField.getBombsNearBy() > 0) {
                        drawHints(canvas, i, j, tileSize);
                    }
                }
                if(GameLogic.getInstance().gameIsWon()) {
                    ((MainActivity) getContext()).gameWon();
                }
            }
        }
    }

    private void drawGameOver(Canvas canvas) {
        int tileSize = getWidth()/GameLogic.GRID_SIZE; // our game will always have the condition width = height
        Field curModelField;
        for (int i = 0; i < GameLogic.GRID_SIZE; i++) {
            for (int j = 0; j < GameLogic.GRID_SIZE; j++) {
                curModelField = GameLogic.getInstance().getFieldAt(i, j);
                if (curModelField.isBombOrigin()) {
                    drawBombOrigin(canvas, i, j, tileSize);
                } else if(curModelField.isBomb() && !curModelField.isFlagged()) {
                    drawBomb(canvas, i, j, tileSize);
                } else if(curModelField.isFlagged()) {
                    drawFlagTile(canvas, i, j, tileSize);
                } else if (!curModelField.isDiscovered()) {
                    drawUndiscoveredTile(canvas, i, j, tileSize);
                } else if(curModelField.isDiscovered()) {
                    drawDiscoveredTile(canvas, i, j, tileSize);
                    if(curModelField.getBombsNearBy() > 0) {
                        drawHints(canvas, i, j, tileSize);
                    }
                }
            }
        }
    }

    /**
     * Performs actions related to exploring a field
     *  if the field is a bomb, we set the field to the bomb
     *  if the field is not a bomb, we expand the area
     * @param touchX x coordinate of the touch
     * @param touchY y coordinate of the touch
     */
    private void performExploreActions(int touchX, int touchY) {
        Field curField = GameLogic.getInstance().getFieldAt(touchX, touchY);
        if(!curField.isDiscovered()) {
            if(curField.isBomb()) {
                GameLogic.getInstance().setFieldAsBombOrigin(touchX, touchY);
                gameOver();
            } else {
                GameLogic.getInstance().expandNearbyEmpty(touchX, touchY);
                GameLogic.getInstance().setFieldAsDiscovered(touchX, touchY);
            }
        }
    }

    /**
     * Performs actions related to placing a flag
     *  if the field is a bomb, we place the flag
     *  if the field is not a bomb, the game ends
     * @param touchX x coordinate of the touch
     * @param touchY y coordinate of the touch
     */
    private void performFlagActions(int touchX, int touchY) {
        Field curField = GameLogic.getInstance().getFieldAt(touchX, touchY);
        if (!curField.isBomb() && !curField.isDiscovered()) {
            gameOver(); // if not bomb and placed flag, game over
        }
        if(!curField.isDiscovered()) {
            GameLogic.getInstance().setFieldAsFlag(touchX, touchY);
        }
    }

    /**
     * decides whether we should place a flag or explore the area
     * @param touchX x coordinate of the touch
     * @param touchY y coordinate of the touch
     */
    private void performTouchableActions(int touchX, int touchY) {
        if(GameLogic.getInstance().getFieldAt(touchX, touchY).isDiscovered()) {
            Snackbar.make(((MainActivity) getContext()).layoutRoot, R.string.msg_explored_tile, Snackbar.LENGTH_SHORT).show();
        } else {
            if( ((MainActivity) getContext()).getChoice() == MainActivity.EXPLORE ) { // if true, then we are exploring
                performExploreActions(touchX, touchY);
            } else if ( ((MainActivity) getContext()).getChoice() == MainActivity.FLAG ){ // then we are placing the flag
                performFlagActions(touchX, touchY);
            }
        }
        invalidate();
        MainActivity.choice = 0; // reset choice
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = ((int)event.getX() / (getWidth()/GameLogic.GRID_SIZE));
            int touchY = ((int)event.getY() / (getHeight()/GameLogic.GRID_SIZE));

            if(((MainActivity) getContext()).isTouchable) {
                performTouchableActions(touchX, touchY);
            } else if(!((MainActivity) getContext()).gameOver) { // untouchable bc no choice has been made
                Snackbar.make(((MainActivity) getContext()).layoutRoot, R.string.choose_action_above, Snackbar.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public void resetGame() {
        GameLogic.getInstance().resetModel();
        ((MainActivity) getContext()).reset();
        invalidate();
    }

    private void gameOver() {
        GameLogic.getInstance().gameOver();
        ((MainActivity) getContext()).gameOver();
        invalidate();
    }
}
