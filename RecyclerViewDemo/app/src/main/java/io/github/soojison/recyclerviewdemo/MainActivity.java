package io.github.soojison.recyclerviewdemo;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import io.github.soojison.recyclerviewdemo.adapter.TodoRecyclerAdapter;
import io.github.soojison.recyclerviewdemo.data.Todo;
import io.github.soojison.recyclerviewdemo.touch.TodoItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TODO_ID = "KEY_TODO_ID";
    public static final int REQUEST_CODE = 101;
    private TodoRecyclerAdapter todoRecyclerAdapter;
    private RecyclerView recyclerTodo;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MainApplication) getApplication()).openRealm();

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

        setUpRecycler();

        // tell the view to use the touch gestures
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(todoRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerTodo);
    }

    private void setUpRecycler() {
        recyclerTodo = (RecyclerView) findViewById(R.id.recyclerTodo);
        recyclerTodo.setHasFixedSize(true); // make sure to use the whole area
        // recycler view can render items in all the list... but also you can do a grid
        // so we need to tell the view that we need a list
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerTodo.setLayoutManager(layoutManager); // now knows it should behave like a linear layout

        todoRecyclerAdapter = new TodoRecyclerAdapter(this,
                ((MainApplication) getApplication()).getRealm());
        recyclerTodo.setAdapter(todoRecyclerAdapter);
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

    @Override
    protected void onDestroy() {
        // close the db!
        super.onDestroy();
        ((MainApplication) getApplication()).closeRealm();
    }

    public void showEdit(int adapterPosition, String todoID) {
        positionToEdit = adapterPosition;
        Intent intentStartEdit = new Intent(this, EditActivity.class);
        intentStartEdit.putExtra(KEY_TODO_ID, todoID);
        startActivityForResult(intentStartEdit, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE) {
                    String todoID  = data.getStringExtra(
                            EditActivity.KEY_TODO);

                    todoRecyclerAdapter.updateTodo(todoID, positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
