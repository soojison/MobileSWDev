package io.github.soojison.twoactivitiesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // don't extract them to the strings.xml
    // those are for the user to view, and these are just for us developers
    public static final String KEY_DATA = "KEY_DATA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText etData = (EditText) findViewById(R.id.etData);
        Button btnOK = (Button) findViewById(R.id.btnOk);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start the second activity
                Intent intentShowDetails = new Intent();
                intentShowDetails.setClass(MainActivity.this, DetailsActivity.class);
                intentShowDetails.putExtra(KEY_DATA, etData.getText().toString());
                startActivity(intentShowDetails);

                // how else can you share data across data?
            }
        });
    }
}
