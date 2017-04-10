package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREF_KEY_CURRENCY = "PREF_KEY_CURRENCY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Edit currency");


        final EditText etCurrency = (EditText) findViewById(R.id.etCurrency);
        etCurrency.setText(getCurrency());
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCurrency.getText().toString().trim().equals("")) {
                    etCurrency.setError("The currency must not be empty");
                } else {
                    Intent intentResult = new Intent(SettingsActivity.this, MainActivity.class);
                    intentResult.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    saveCurrency(etCurrency.getText().toString());
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

    private void saveCurrency(String currency) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_CURRENCY, currency);
        editor.commit();
    }

    public String getCurrency() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(PREF_KEY_CURRENCY, "$");
    }
}
