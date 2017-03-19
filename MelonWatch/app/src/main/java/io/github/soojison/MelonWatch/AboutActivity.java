package io.github.soojison.MelonWatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolBarAbout = (Toolbar) findViewById(R.id.toolBarAbout);
        setSupportActionBar(toolBarAbout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolBarAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ArrayList<String> aboutArray = new ArrayList<>();
        aboutArray.add("Icon Design by Sooji Son");
        aboutArray.add("Features include: Toolbar, Multiple activities, TimerTask, Drawable import");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.layout_listview, aboutArray);

        ListView listAbout = (ListView) findViewById(R.id.listAbout);
        listAbout.setAdapter(adapter);

    }
}
