package hu.ait.tictactoe;

import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import hu.ait.tictactoe.model.TicTacToeModel;
import hu.ait.tictactoe.view.TicTacToeView;

public class MainActivity extends AppCompatActivity {

    private Chronometer mChronometer;


    //can't use findViewId here because it's not generated yet -- happens before onCreate
    private TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChronometer = new Chronometer(this);
        tvData = (TextView) findViewById(R.id.tvData);



        // find view by id in the view
        // Create an instance of the view and call methods on it to edit the view properties?
        // getcontext casted to mainactivity can call methods too?
        final TicTacToeView gameView = (TicTacToeView) findViewById(R.id.gameView);

        Button btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.resetGame();
            }
        });

        ShimmerFrameLayout shimmerFrameLayout =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();

    }

    public void stopChronos() {
        mChronometer.stop();
    }

    public void startChronos() {
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
    }

    public void getElapsedTime() {
        final LinearLayout root = (LinearLayout) findViewById(R.id.activity_main);
        long elapsedMillis = SystemClock.elapsedRealtime() - mChronometer.getBase();
        int mins = (int) (elapsedMillis) / 60000; // I'm assuming the player is not gonna go for hours
        int secs = (int) (elapsedMillis - mins * 60000) / 1000;
        String time = "Time elapsed: " + mins + "' " + secs + "\"";
        Snackbar.make(root, time, Snackbar.LENGTH_LONG).show();
    }


    public void setMessage(String text) {
        tvData.setText(text);
    }
}
