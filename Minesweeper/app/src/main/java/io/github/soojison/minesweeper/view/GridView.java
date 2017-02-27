package io.github.soojison.minesweeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import io.github.soojison.minesweeper.model.GameLogic;

public class GridView extends View {

    private Paint paintBG;
    private Paint paintLine;
    private Paint paintTextR;
    private Paint paintTextG;
    private Paint paintTextB;
    private Paint paintDiscoveredBG;

    private void setPaintObjAttrs() {
        paintBG.setColor(Color.GRAY);
        paintLine.setColor(Color.DKGRAY);
        paintLine.setStrokeWidth(3);
        paintTextR.setColor(Color.RED);
        paintTextG.setColor(Color.GREEN);
        paintTextB.setColor(Color.BLUE);
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
        paintDiscoveredBG = new Paint();

        setPaintObjAttrs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBG);

        drawPlayers(canvas);

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

    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (GameLogic.getInstance().getModelField(i, j) == GameLogic.DISCOVERED) {
                    canvas.drawRect(i * getWidth() / 5, j * getHeight() / 5,
                            (i + 1) * getWidth() / 5, (j + 1) * getHeight() / 5,
                            paintDiscoveredBG);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int touchX = ((int)event.getX() / (getWidth()/5));
            int touchY = ((int)event.getY() / (getHeight()/5));

            if(GameLogic.getInstance().getModelField(touchX, touchY) == GameLogic.UNDISCOVERED) {
                GameLogic.getInstance().setModelField(touchX, touchY, GameLogic.DISCOVERED);
            }
            invalidate();
        }
        return true;
    }
}
