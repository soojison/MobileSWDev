package io.github.soojison.moneyexchangeretrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.soojison.moneyexchangeretrofit.data.MoneyResult;
import io.github.soojison.moneyexchangeretrofit.network.MoneyAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    //http://www.jsonschema2pojo.org/
    // settings: class name: MoneyResult
    // source type: JSON
    // Annotation style: Gson
    // uncheck include getters and setters

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // base url is just the server address without slash
        // http://api.fixer.io/latest?base=USD
        // [protocol][  host ][ path ][query params]
        // [     base url    ]
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fixer.io") // make sure you don't have a typo!
                .addConverterFactory(GsonConverterFactory.create()) // tell that we are using GSON
                .build();

        // mix the host (that has the host url) with the interface (that has path & parameters)
        // retrofit library will generate an object that is implementing the interface,
        // we don't care how it works, just need the method to go it
        final MoneyAPI moneyAPI = retrofit.create(MoneyAPI.class);

        final TextView tvData = (TextView) findViewById(R.id.tvData);
        Button btnGet = (Button) findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prepared the network call, everything ready for network operation
                Call<MoneyResult> callUsd = moneyAPI.getRatesForUSD("usd");
                // we don't know when we will get back the result, similar to firebase, so use callback
                callUsd.enqueue(new Callback<MoneyResult>() {
                    @Override
                    public void onResponse(Call<MoneyResult> call, Response<MoneyResult> response) {
                        tvData.setText(response.body().getRates().gethUF()+"\n"+
                        response.body().getRates().geteUR()); // body is the money result obj
                    }
                    // no async, no input stream, etc., no event bus, etc. retrofit is a good library!

                    @Override
                    public void onFailure(Call<MoneyResult> call, Throwable t) {
                        tvData.setText(t.getLocalizedMessage());
                    }
                });
            }
        });
    }
}
