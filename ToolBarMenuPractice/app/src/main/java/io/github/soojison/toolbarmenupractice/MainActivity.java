 package io.github.soojison.toolbarmenupractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {

     public static final String KEY_ETDATA = "KEY_ETDATA";
     private static Toolbar toolbar;
     private MenuItem mSearchAction;
     private boolean isSearchOpened = false;
     private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        populateToolbar();
        setSupportActionBar(toolbar);

        final EditText etData = (EditText) findViewById(R.id.etData);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "nav pressed",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(KEY_ETDATA, etData.getText().toString());
                intent.setClass(MainActivity.this, HelloActivity.class);
                startActivity(intent);
            }
        });
    }

     public static void populateToolbar() {
         toolbar.setLogo(R.mipmap.ic_launcher_round);
         toolbar.setLogoDescription("An icon");
         toolbar.setTitle("BIG MUSHROOMS");
         toolbar.setSubtitle("small mushrooms");
         toolbar.setNavigationIcon(android.R.drawable.ic_menu_add);
         toolbar.inflateMenu(R.menu.menu);
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch(item.getItemId()) {
             case R.id.action_search:
                 Toast.makeText(this, "Bloop", Toast.LENGTH_SHORT).show();
                 break;
             case R.id.action_star:
                 Toast.makeText(this, "STAR", Toast.LENGTH_SHORT).show();
                 break;
             default:
                 break;
         }
         return true;
     }

     @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
         mSearchAction = menu.findItem(R.id.action_search);
         return super.onPrepareOptionsMenu(menu);
     }
 }
