package io.github.soojison.MelonWatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layoutMarkContent)
    LinearLayout layoutMarkContent;

    @BindView(R.id.btnStart)
    Button btnStart;

    @BindView(R.id.btnStop)
    Button btnStop;

    @BindView(R.id.btnReset)
    Button btnReset;

    @BindView(R.id.tvTime)
    TextView tvTime;

    private Timer myTimer = null;
    private long startTime = 0;

    private class myShowTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTime.setText(getTime());
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTimer == null) {
                    startTime = System.currentTimeMillis();
                    myTimer = new Timer();
                    myTimer.schedule(new myShowTimerTask(), 0, 1);
                    tvTime.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                    layoutMarkContent.removeAllViews();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myTimer != null) {
                    myTimer.cancel();
                    myTimer = null;
                    tvTime.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMarkContent.removeAllViews();
                tvTime.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                tvTime.setText("00:00:00");
                if(myTimer != null) {
                    myTimer.cancel();
                    myTimer = null;
                }

            }
        });

    }

    @OnClick(R.id.btnMark)
    public void savePressed(Button btn) {
        if(myTimer != null) {
            final View viewMark =
                    getLayoutInflater().inflate(R.layout.layout_mark, null);
            TextView tvMark = (TextView) viewMark.findViewById(R.id.tvMark);
            tvMark.setText(getTime());


            Button btnDel = (Button) viewMark.findViewById(R.id.btnDelete);
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutMarkContent.removeView(viewMark);
                }
            });

            layoutMarkContent.addView(viewMark, 0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myTimer != null) {
            myTimer.cancel();
            myTimer = null;
        }
    }

    private String getTime() {
        long millis = System.currentTimeMillis() - startTime;
        return new SimpleDateFormat("mm:ss:SS").format(new Date(millis));
    }
}
