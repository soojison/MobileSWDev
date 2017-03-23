package io.github.soojison.andwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layoutListContent)
    LinearLayout layoutListContent;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.etExpenses)
    EditText etExpenses;

    @BindView(R.id.etCategory)
    EditText etCategory;

    @BindView(R.id.toggleInOut)
    ToggleButton toggleInOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.expand_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuDelete:
                layoutListContent.removeAllViews();
                etCategory.getText().clear();
                etExpenses.getText().clear();
                etCategory.clearFocus();
                etExpenses.clearFocus();
                break;
            case R.id.menuSummary:
                // open a new activity
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick(R.id.btnSave)
    public void pressButton(Button btn) {

        boolean etCategoryEmpty = etCategory.getText().toString().equals("");
        boolean etExpensesEmpty = etExpenses.getText().toString().equals("");

        if(etCategoryEmpty) {
            etCategory.setError("Your category must not be empty");
        } else if(etExpensesEmpty) {
            etExpenses.setError("Your amount must not be empty");
        } if(!etCategoryEmpty && !etExpensesEmpty) {
            final View viewEntry =
                    getLayoutInflater().inflate(R.layout.list_entry_row, null);
            TextView tvCategory = (TextView) viewEntry.findViewById(R.id.tvCategory);
            TextView tvExpenses = (TextView) viewEntry.findViewById(R.id.tvPrice);
            ImageView imageView = (ImageView) viewEntry.findViewById(R.id.imgView);

            if(toggleInOut.isChecked()) { // then it's income
                imageView.setImageResource(R.drawable.income);
            } else {
                imageView.setImageResource(R.drawable.expense);
            }

            tvCategory.setText(etCategory.getText().toString());
            tvExpenses.setText(etExpenses.getText().toString());

            layoutListContent.addView(viewEntry);
        }

    }

}
