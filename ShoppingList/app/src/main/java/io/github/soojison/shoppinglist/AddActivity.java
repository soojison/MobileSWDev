package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import io.github.soojison.shoppinglist.data.Category;
import io.github.soojison.shoppinglist.data.Item;

public class AddActivity extends AppCompatActivity {

    private EditText etName;

    private EditText etDescription;

    private EditText etPrice;

    private Item resultItem;

    // TODO: make sure the ET thing is not empty

    //TODO: make sure the prices are only two digits

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initializeToolBar();

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPrice = (EditText) findViewById(R.id.etPrice);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choices_array, android.R.layout.simple_spinner_item);
        // specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to spinner
        spinner.setAdapter(adapter);
    }

    public void initializeToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add New Item");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean isEmpty() {
        if(etName.getText().toString().trim().equalsIgnoreCase("")) {
            etName.setError("The name cannot be empty");
            etName.requestFocus();
        } else if(etDescription.getText().toString().trim().equalsIgnoreCase("")) {
            etDescription.setError("The description cannot be empty");
            etDescription.requestFocus();
        } else if(etPrice.getText().toString().trim().equalsIgnoreCase("")) {
            etPrice.setError("The price cannot be empty");
            etPrice.requestFocus();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuDone:
                Toast.makeText(this, "Done Pressed", Toast.LENGTH_SHORT).show();
                if(!isEmpty()) {
                    resultItem = new Item(etName.getText().toString(), etDescription.getText().toString(),
                            33, false, Category.ENTERTAINMENT); //TODO: handle categories
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void finish() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("passed_item", (Parcelable) resultItem);
        setResult(RESULT_OK, returnIntent);
        super.finish();
    }
}
