package hu.ait.tictactoe.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeView extends View {

    // factor out so it doesn't get created every time onDraw is called
    private Paint paintBg;
    private Paint paintLine;
    private Paint paintRedCircle;

    private List<PointF> coords = new ArrayList<PointF>();

    public TicTacToeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.BLACK);
        paintBg.setStyle(Paint.Style.FILL); // fills the area

        paintLine = new Paint();
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);

        paintRedCircle = new Paint();
        paintRedCircle.setColor(Color.RED);
        paintRedCircle.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) { // the entire drawable area
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        canvas.drawLine(0, 0, getWidth(), getHeight(), paintLine);

        // shortcut: coords.for
        for (PointF coord : coords) {
            //canvas.drawCircle(coord.x, coord.y, 50, paintLine);
            // Pekler: "HAHA! I have circles"
            canvas.drawCircle(coord.x, coord.y, 50, paintRedCircle);
        }
        
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            coords.add(new PointF(event.getX(), event.getY()));
            
            // invalidate redraws everything on the view
            invalidate();
            
            
        }

        return true;

    }
}
