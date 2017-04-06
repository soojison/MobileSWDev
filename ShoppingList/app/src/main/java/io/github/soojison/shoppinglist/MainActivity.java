package io.github.soojison.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    public static final int EDIT_ITEM_REQUEST_CODE = 2;
    public static final String PASSED_ITEM = "passed item";
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerItemView;
    private RelativeLayout viewRecyclerEmpty;


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
        recyclerAdapter = new RecyclerAdapter(this, recyclerItemView, viewRecyclerEmpty);
        recyclerItemView.setAdapter(recyclerAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(recyclerAdapter, recyclerItemView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerItemView);

        toggleEmptyRecycler();
    }

    private void initializeToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void toggleEmptyRecycler() {
        recyclerAdapter.toggleEmptyRecycler();
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
                new AlertDialog.Builder(this).setTitle(R.string.alert_dialog_title)
                        .setMessage(R.string.alert_dialog_message)
                        .setPositiveButton(R.string.alert_dialog_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //continue with delete
                                recyclerAdapter.deleteAll();
                            }
                        })
                        .setNegativeButton(R.string.alert_dialog_negative, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing
                            }
                        })
                        .setIcon(R.drawable.ic_report_problem)
                        .show();
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
            Item passedItem = (Item) data.getExtras().get(PASSED_ITEM);
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
