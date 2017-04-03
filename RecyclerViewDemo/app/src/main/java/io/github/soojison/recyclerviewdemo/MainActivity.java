package io.github.soojison.recyclerviewdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import io.github.soojison.recyclerviewdemo.adapter.TodoRecyclerAdapter;
import io.github.soojison.recyclerviewdemo.data.Todo;
import io.github.soojison.recyclerviewdemo.touch.TodoItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    private TodoRecyclerAdapter todoRecyclerAdapter;
    private RecyclerView recyclerTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init the floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTodoDialogue();
            }
        });

        recyclerTodo = (RecyclerView) findViewById(R.id.recyclerTodo);
        recyclerTodo.setHasFixedSize(true); // make sure to use the whole area
        // recycler view can render items in all the list... but also you can do a grid
        // so we need to tell the view that we need a list
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTodo.setLayoutManager(layoutManager); // now knows it should behave like a linear layout

        todoRecyclerAdapter = new TodoRecyclerAdapter(this);
        recyclerTodo.setAdapter(todoRecyclerAdapter);

        // tell the view to use the touch gestures
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(todoRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerTodo);
    }

    public void showAddTodoDialogue() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Todo");
        final EditText etTodoText = new EditText(this);
        // you could create your own layout file and inflate and add here
        builder.setView(etTodoText);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                todoRecyclerAdapter.addTodo(etTodoText.getText().toString());
                recyclerTodo.scrollToPosition(0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
