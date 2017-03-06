package io.github.soojison.highlowgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    // override default behavior
    // super method is important in the lifecycle methods, but here if you use it,
    // it won't override anything
    @Override
    public void onBackPressed() {
        /*
         * Pekler style humor:
         * Toast.makeText(this, "Haha you never exit!", Toast.LENGTH_SHORT).show();
         */

        // You can't just start the mainactivity again,
        // because it just adds another activity on the stack
        // so you have to use intents to clear the backstack
        Intent showMain = new Intent(this, MainActivity.class);
        showMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(showMain);
    }
}
