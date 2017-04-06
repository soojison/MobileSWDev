package io.github.soojison.recyclerviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import io.github.soojison.recyclerviewdemo.data.Todo;
import io.realm.Realm;

// on create
// what was the id that was sent to me? --> query from the realm database
// event handler for save button

public class EditActivity extends AppCompatActivity {
    public static final String KEY_TODO = "KEY_TODO";
    private EditText etTodoText;
    private CheckBox cbTodoDone;
    private Todo todoToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (getIntent().hasExtra(MainActivity.KEY_TODO_ID)) {
            String todoID = getIntent().getStringExtra(MainActivity.KEY_TODO_ID);
            todoToEdit = getRealm().where(Todo.class)
                    .equalTo("todoID", todoID)
                    .findFirst();
        }

        etTodoText = (EditText) findViewById(R.id.etTodoText);
        cbTodoDone = (CheckBox) findViewById(R.id.cbTodoDone);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });

        if (todoToEdit != null) {
            etTodoText.setText(todoToEdit.getTodoText());
            cbTodoDone.setChecked(todoToEdit.isDone());
        }
    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealm();
    }

    private void saveTodo() {
        if ("".equals(etTodoText.getText().toString())) {
            etTodoText.setError("can not be empty");
        } else {
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            todoToEdit.setTodoText(etTodoText.getText().toString());
            todoToEdit.setDone(cbTodoDone.isChecked());
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_TODO, todoToEdit.getTodoID());
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }
}

