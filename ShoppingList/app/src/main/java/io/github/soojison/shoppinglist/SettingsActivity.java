package io.github.soojison.shoppinglist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    public static final String PREF_KEY_CURRENCY = "PREF_KEY_CURRENCY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.setting_activity_title));

        setupDialogSize();

        final EditText etCurrency = (EditText) findViewById(R.id.etCurrency);
        etCurrency.setText(getCurrency());
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCurrency.getText().toString().trim().equals("")) {
                    etCurrency.setError(getResources().getString(R.string.et_currency_empty_error));
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

    private void setupDialogSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        setContentView(R.layout.activity_settings);
        getWindow().setLayout(screenWidth, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onBackPressed() {
        Intent showMain = new Intent(this, MainActivity.class);
        showMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_CANCELED, showMain);
        finish();
    }

    private void saveCurrency(String currency) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PREF_KEY_CURRENCY, currency);
        editor.apply();
    }

    public String getCurrency() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        return sp.getString(PREF_KEY_CURRENCY, getResources().getString(R.string.dollar_sign));
    }
}
