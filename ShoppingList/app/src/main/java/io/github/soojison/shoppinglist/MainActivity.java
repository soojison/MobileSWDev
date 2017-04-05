package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.github.soojison.shoppinglist.adapter.RecyclerAdapter;
import io.github.soojison.shoppinglist.data.Item;
import io.github.soojison.shoppinglist.touch.ItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_ITEM_REQUEST_CODE = 1;
    private static final int CURRENCY_REQUEST_CODE = 2;
    public static RecyclerAdapter recyclerAdapter;
    public static RecyclerView recyclerItemView;
    private static RelativeLayout viewRecyclerEmpty;

    //TODO: icon source https://www.iconfinder.com/iconsets/flat-icons-19
    //TODO: multiple quantities, indicate how much u wanna buy sth?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolBar();

        recyclerItemView = (RecyclerView) findViewById(R.id.recyclerItemView);
        viewRecyclerEmpty = (RelativeLayout) findViewById(R.id.viewRecyclerEmpty);
        recyclerItemView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItemView.setLayoutManager(layoutManager);

        recyclerAdapter = new RecyclerAdapter(this);
        recyclerItemView.setAdapter(recyclerAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(recyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerItemView);

        toggleEmptyView();

    }

    public static void toggleEmptyView() {
        if (recyclerAdapter.getItemCount() == 0) {
            recyclerItemView.setVisibility(View.GONE);
            viewRecyclerEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerItemView.setVisibility(View.VISIBLE);
            viewRecyclerEmpty.setVisibility(View.GONE);
        }
    }

    private void initializeToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                Intent addIntent = new Intent(this, AddActivity.class);
                startActivityForResult(addIntent, NEW_ITEM_REQUEST_CODE);
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
        if(requestCode == NEW_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            Item passedItem = (Item) data.getExtras().get("passed_item");
            recyclerAdapter.addItem(passedItem.getName(), passedItem.getDescription(),
                    passedItem.getPrice(), passedItem.getCategory());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerAdapter.closeRealm();
    }

}
