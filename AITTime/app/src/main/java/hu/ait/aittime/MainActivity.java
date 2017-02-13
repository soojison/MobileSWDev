package hu.ait.aittime;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // One method should probably do only one thing, not multiple things
    // so you should extract the things into several methods.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // used to figure out where the snackbar should be situated
        final LinearLayout layoutRoot = (LinearLayout) findViewById(R.id.activity_main);

        // Everything you put on a screen is extended from the View class
        Button btnTime = (Button) findViewById(R.id.btnTime);
        // R is a class, id is a class inside the class, and btnTime is a value
        // findViewById requires an integer, so that gets the integer identifier
        // in Project mode: app > generated > source > r > debug > hu.ait.aittime > R.class
        // --> A final class that has all the integer ids for the things
        // --> Just don't touch the file at all, if it says there is an error in the R file,
        //     it must be in the resources files, so check that.

        final TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        // final because it's a constant, a fixed reference

        final EditText username = (EditText) findViewById(R.id.usrName);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();

                String time = getString(R.string.txt_date_message,
                        name, new Date(System.currentTimeMillis()).toString());

                Toast.makeText(
                        MainActivity.this,
                        time,
                        Toast.LENGTH_LONG).show();
                // independent of the application, so you need a context for where the toast occurred

                tvStatus.setText(time);

                Snackbar.make(layoutRoot, time, Snackbar.LENGTH_LONG).show();

            }
        });
    }
}
