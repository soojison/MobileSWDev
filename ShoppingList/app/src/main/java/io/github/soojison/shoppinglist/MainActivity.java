package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.soojison.shoppinglist.adapter.RecyclerAdapter;
import io.github.soojison.shoppinglist.data.Item;

public class MainActivity extends AppCompatActivity {

    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerItem;

    //TODO: multiple quantities, indicate how much u wanna buy sth?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolBar();

        recyclerItem = (RecyclerView) findViewById(R.id.recyclerItemView);
        recyclerItem.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItem.setLayoutManager(layoutManager);

        // TODO: if the list is empty say that the list is empty in the view
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
                Intent intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, 420);

                break;
            case R.id.menuDeleteAll:
                recyclerAdapter.deleteAll();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 420 && resultCode == RESULT_OK) {
            Log.i("TAG_INTENT", "a thing passed");
            Item passedItem = (Item) data.getExtras().get("passed_item");
            Log.i("TAG_INTENT", passedItem.getName() + passedItem.getDescription());
            recyclerAdapter.addItem(passedItem);
        }
    }
}
