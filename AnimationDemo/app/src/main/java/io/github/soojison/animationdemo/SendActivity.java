package io.github.soojison.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class SendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        final Button btnSend = (Button) findViewById(R.id.btnSend);
        final TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
        final Animation animation = AnimationUtils.loadAnimation(SendActivity.this, R.anim.send_anim);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMessage.startAnimation(animation);
            }
        });
    }
}
