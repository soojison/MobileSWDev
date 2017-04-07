package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.github.soojison.shoppinglist.data.Currency;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Edit currency");

        final EditText etCurrency = (EditText) findViewById(R.id.etCurrency);
        etCurrency.setText(Currency.getInstance().getCurrency());
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCurrency.getText().toString().trim().equals("")) {
                    etCurrency.setError("The currency must not be empty");
                } else {
                    Intent intentResult = new Intent(SettingsActivity.this, MainActivity.class);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Currency.getInstance().setCurrency(etCurrency.getText().toString());
                    setResult(RESULT_OK, intentResult);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent showMain = new Intent(this, MainActivity.class);
        showMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_CANCELED, showMain);
    }
}
