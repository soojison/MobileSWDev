 package io.github.soojison.toolbarmenupractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {

     private Toolbar toolbar;
     private MenuItem mSearchAction;
     private boolean isSearchOpened = false;
     private EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
