package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import io.github.soojison.shoppinglist.data.Item;

public class AddActivity extends AppCompatActivity {


    private EditText etName;
    private EditText etDescription;
    private EditText etPrice;
    private Item resultItem;
    private int category;

    // TODO: some animation??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initializeToolBar();

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etPrice = (EditText) findViewById(R.id.etPrice);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        initializeSpinner(spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void initializeToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_activity_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initializeSpinner(Spinner spinner) {
        // adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.choices_array, android.R.layout.simple_spinner_item);
        // specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    public boolean etIsEmpty() {
        if(etName.getText().toString().trim().equalsIgnoreCase("")) {
            etName.setError(getString(R.string.et_name_empty_error));
            etName.requestFocus();
        } else if(etDescription.getText().toString().trim().equalsIgnoreCase("")) {
            etDescription.setError(getString(R.string.et_desc_empty_error));
            etDescription.requestFocus();
        } else if(etPrice.getText().toString().trim().equalsIgnoreCase("")) {
            etPrice.setError(getString(R.string.et_price_empty_error));
            etPrice.requestFocus();
        } else if(longerThanTwo(etPrice.getText().toString().trim())) {
            etPrice.setError(getString(R.string.et_price_decimal_error));
            etPrice.requestFocus();
        } else {
            return false;
        }
        return true;
    }

    private boolean longerThanTwo(String value) {
        String[] decimal = value.split("\\.");
        return decimal.length != 1 && decimal[1].length() > 2;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuDone:
                if(!etIsEmpty()) {
                    resultItem = new Item(
                            etName.getText().toString(),
                            etDescription.getText().toString(),
                            Double.parseDouble(etPrice.getText().toString()),
                            false,
                            category);
                    passIntent();
                    finish();
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void passIntent() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(MainActivity.PASSED_ITEM, (Parcelable) resultItem);
        setResult(RESULT_OK, returnIntent);
    }

}
