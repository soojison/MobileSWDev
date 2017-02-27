package hu.ait.tictactoe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import hu.ait.tictactoe.MainActivity;
import hu.ait.tictactoe.model.TicTacToeModel;

public class TicTacToeView extends View {

    // factor out so it doesn't get created every time onDraw is called
    private Paint paintBg;
    private Paint paintLine;
    private Paint redLine;
    private boolean isTouchable = true;



    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL); // fills the area

        redLine = new Paint();
        redLine.setColor(Color.RED);
        redLine.setStyle(Paint.Style.STROKE);
        redLine.setStrokeWidth(5);

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) { // the entire drawable area
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        drawGameGrid(canvas);

        drawPlayers(canvas);
    }

    /**
     * detects where the touch occurred, draws the players
     * @param canvas
     */
    private void drawPlayers(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (TicTacToeModel.getInstance().getField(i,j) == TicTacToeModel.CIRCLE) {

                    // draw a circle at the center of the field

                    // X coordinate: left side of the square + half width of the square
                    float centerX = i * getWidth() / 3 + getWidth() / 6;
                    float centerY = j * getHeight() / 3 + getHeight() / 6;
                    int radius = getHeight() / 6 - 2;

                    canvas.drawCircle(centerX, centerY, radius, paintLine);

                } else if (TicTacToeModel.getInstance().getField(i,j) == TicTacToeModel.CROSS) {
                    canvas.drawLine(i * getWidth() / 3, j * getHeight() / 3,
                            (i + 1) * getWidth() / 3,
                            (j + 1) * getHeight() / 3, redLine);

                    canvas.drawLine((i + 1) * getWidth() / 3, j * getHeight() / 3,
                            i * getWidth() / 3, (j + 1) * getHeight() / 3, redLine);
                }
            }
        }
    }

    // cmd + alt + t: method extraction
    private void drawGameGrid(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintLine);
        // two horizontal lines
        canvas.drawLine(0, getHeight() / 3, getWidth(), getHeight() / 3,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 3, getWidth(),
                2 * getHeight() / 3, paintLine);

        // two vertical lines
        canvas.drawLine(getWidth() / 3, 0, getWidth() / 3, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight(),
                paintLine);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!isTouchable) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int touchX = ((int)event.getX() / (getWidth()/3));
            int touchY = ((int)event.getY() / (getHeight()/3));

            if(TicTacToeModel.getInstance().getField(touchX, touchY) == TicTacToeModel.EMPTY) {
                TicTacToeModel.getInstance().setField(touchX, touchY,
                        TicTacToeModel.getInstance().getNextPlayer());
                TicTacToeModel.getInstance().changeNextPlayer();
                invalidate();

                String next = "O";
                if (TicTacToeModel.getInstance().getNextPlayer() == TicTacToeModel.CROSS) {
                    next = "X";
                }

                if(TicTacToeModel.getInstance().isBoardFull()) {
                    ((MainActivity)getContext()).setMessage("Game over with a tie");
                    isTouchable = false;
                } else {
                    short winner = TicTacToeModel.getInstance().checkWinner();
                    if (winner == 0) {
                        ((MainActivity) getContext()).setMessage("Next player is " + next);
                    } else {
                        String winnerS = (winner == 1) ? "Circle" : "Cross";
                        ((MainActivity) getContext()).setMessage("Winner is " + winnerS);
                        isTouchable = false;
                    }
                }
            }
        }

        return true;
    }

    // compare the width and the height of the screen
    // and get the smaller value to set the size of the view
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

    public void resetGame() {
        TicTacToeModel.getInstance().resetModel();
        invalidate();
        isTouchable = true;
        ((MainActivity) getContext()).setMessage("Touch the game area to play");
    }
}
