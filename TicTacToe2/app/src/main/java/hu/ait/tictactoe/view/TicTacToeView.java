package hu.ait.tictactoe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TicTacToeView extends View {

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) { // the entire drawable area
        super.onDraw(canvas);

        // draw a rectangle
        Paint paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL); // fills the area

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);
    }
}
