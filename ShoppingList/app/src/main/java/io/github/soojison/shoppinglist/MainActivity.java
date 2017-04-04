package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.soojison.shoppinglist.adapter.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolBar();

        recyclerItem = (RecyclerView) findViewById(R.id.recyclerItem);
        recyclerItem.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItem.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter(this);
        recyclerItem.setAdapter(recyclerAdapter);
    }

    public void initializeToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("All");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuAdd:
                startActivity(new Intent().setClass(MainActivity.this, AddActivity.class));
                break;
            case R.id.menuDeleteAll:
                Toast.makeText(this, "Deleteall", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}
