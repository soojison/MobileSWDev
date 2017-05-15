package io.github.soojison.intentdemo;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnIntent = (Button) findViewById(R.id.btnIntent);
        btnIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void startSearch(String keyword) {
        Intent intentSearch = new Intent();
        intentSearch.setAction(Intent.ACTION_WEB_SEARCH);
        intentSearch.putExtra(SearchManager.QUERY, keyword);
        startActivity(intentSearch);
    }
}
