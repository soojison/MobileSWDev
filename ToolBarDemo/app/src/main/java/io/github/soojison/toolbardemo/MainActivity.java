package io.github.soojison.toolbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // this method returns a bool, which tells us that we're creating a menu

        // indicates that menu obj in the parameter
        // we should add the R.menu into the parameter menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this, "HELP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_star:
                Toast.makeText(this, "STAR", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
