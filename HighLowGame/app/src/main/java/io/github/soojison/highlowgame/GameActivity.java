package io.github.soojison.highlowgame;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static final String KEY_RAND = "KEY_RAND";
    public static final String KEY_GUESSES = "KEY_GUESSES";
    private int random;
    private int numGuesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // when screen rotates, you get a new number and you confuse the user
        // you could lock the orientation, but that's just a quickfix
        // so you use onSaveInstanceState(Bundle outState) to save the generated number
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_RAND)) {
            random = savedInstanceState.getInt(KEY_RAND);
            numGuesses = savedInstanceState.getInt(KEY_GUESSES);
        } else {
            generateNewRandom();
            numGuesses = 0;
        }

        final EditText etGuess = (EditText) findViewById(R.id.etGuess);
        Button btnGuess = (Button) findViewById(R.id.btnGuess);
        final TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        final TextView tvNumGuesses = (TextView) findViewById(R.id.tvNumGuesses);

        btnGuess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etGuess.getText().toString().equals("")) {
                    try { // if you put #s other than ints, the app will crash. so have a try catch block to alert the user
                        int myNumber = Integer.parseInt(etGuess.getText().toString());
                        numGuesses++;
                        tvNumGuesses.setText("Number of guesses: " + String.valueOf(numGuesses));
                        if(random == myNumber) {
                            // pekler: is it you have won or you've won? how can I learn english if it's both?
                            tvStatus.setText("You have won!");
                            startActivity(new Intent(GameActivity.this, ResultActivity.class));

                        } else if(random < myNumber) {
                            // pekler: is it bigger? higher?
                            // student: both
                            // student: you could also do larger
                            tvStatus.setText("Your number is higher.");
                        } else if (random > myNumber) {
                            tvStatus.setText("Your number is lower");
                        }
                    } catch (NumberFormatException e) {
                        etGuess.setError("This must be an integer number!");
                    }
                } else {
                    // how to tell the user to stop being dumb and input a number
                    // EditText has a default method so pls use this!
                    etGuess.setError("Come on write something here you piece of pie");
                }
            }
        });
    }

    private void generateNewRandom() { // since this is the start of the game
        random = new Random(System.currentTimeMillis()).nextInt(99);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // this will save the number in the activity
        outState.putInt(KEY_RAND, random);
        outState.putInt(KEY_GUESSES, numGuesses);
    }
}
