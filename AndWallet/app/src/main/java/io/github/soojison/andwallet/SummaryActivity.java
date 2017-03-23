package io.github.soojison.andwallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import io.github.soojison.andwallet.data.Summary;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbarSummary = (Toolbar) findViewById(R.id.toolbarSummary);
        setSupportActionBar(toolbarSummary);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Summary");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbarSummary.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView tvIncome = (TextView) findViewById(R.id.tvIncome);
        TextView tvExpenses = (TextView) findViewById(R.id.tvExpenses);
        TextView tvBalanceSum = (TextView) findViewById(R.id.tvBalanceSum);

        tvIncome.setText(String.format("Income: $%s", Summary.getInstance().getIncome()));
        tvExpenses.setText(String.format("Expenses: S%s", Summary.getInstance().getExpenses()));
        tvBalanceSum.setText(String.format("Balance: $%s", Summary.getInstance().getBalance()));
    }


}
