package io.github.soojison.animationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    // pekler: "how do we play one animation and then the other? It's a requirement from the customer"
    // pekler: "We don't do it. Go to hell. I leave this job."

    // pekler: design team has done a good job, it's always the developers who have to implement. that's life

    // how to play the animation and then start another activity?
    // waht? how can we do dat? we can give an event to the animation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnPlay = (Button) findViewById(R.id.btnPlay);
        final Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.play_anim);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.startAnimation(animation);
            }
        });

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Here, you can start a new activity or do whatever!
                // WAY simpler than using Timers and thinking about UI Thread
                Toast.makeText(MainActivity.this, "Animation ended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
