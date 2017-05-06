package io.github.soojison.aitweather;

import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.aitweather.adapter.WeatherAdapter;
import io.github.soojison.aitweather.api.WeatherApi;
import io.github.soojison.aitweather.data.WeatherResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_CITY_NAME = "KEY_CITY_NAME";

    public static final String HTTP_API_OPENWEATHERMAP_ORG = "http://api.openweathermap.org";
    public static final String MY_API_KEY = "13ecf6e64cda416ef869f94f53a99417";

    public WeatherApi weatherApi;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvCurrentTemp)
    TextView tvCurrentTemp;

    @BindView(R.id.imgWeatherIcon)
    ImageView imgWeatherIcon;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvWind)
    TextView tvWind;

    @BindView(R.id.tvCloud)
    TextView tvCloud;

    @BindView(R.id.tvPressure)
    TextView tvPressure;

    @BindView(R.id.tvHumidity)
    TextView tvHumidity;

    @BindView(R.id.tvSunrise)
    TextView tvSunrise;

    @BindView(R.id.tvSunset)
    TextView tvSunSet;

    // TODO: icons, misc weather stuff
    // TODO: metric / imperial settings in shared preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(KEY_CITY_NAME)) { // if there is no intent we can't do anything
            String cityName = getIntent().getStringExtra(KEY_CITY_NAME);
            initializeToolbar();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HTTP_API_OPENWEATHERMAP_ORG)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            weatherApi = retrofit.create(WeatherApi.class);

            displayWeatherInfo(cityName);


        }


    }

    private void displayWeatherInfo(String cityName) {
        //todo: metric/imperial

        // todo: handle loading while loading api data
        Call<WeatherResult> call = weatherApi.getCurrentWeather(cityName, "metric", MY_API_KEY);
        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                // todo: method extraciton for filling out body
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(response.body().getName());
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
                tvTitle.setText("Weather in " + response.body().getName() + ", " + response.body().getSys().getCountry());
                tvCurrentTemp.setText(response.body().getMain().getTemp()+"°C");
                tvDescription.setText(response.body().getWeather().get(0).getDescription());
                String iconURL = "http://openweathermap.org/img/w/" +
                        response.body().getWeather().get(0).getIcon() + ".png";
                Glide.with(getApplicationContext()).load(iconURL).into(imgWeatherIcon);
                tvWind.setText(response.body().getWind().getSpeed() + "m/s in the direction of "
                        + response.body().getWind().getDeg());
                tvCloud.setText(response.body().getClouds().getAll() + "%");
                tvPressure.setText(response.body().getMain().getPressure() + "hPa");
                tvHumidity.setText(response.body().getMain().getHumidity() + "%");
                tvSunrise.setText(response.body().getSys().getSunrise() + "TIME");
                tvSunSet.setText(response.body().getSys().getSunset() + "TIME");
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                String errorType;
                String errorDesc;
                if(t instanceof IOException) {
                    errorType = "Timeout";
                    errorDesc = String.valueOf(t.getCause());
                } else if (t instanceof IllegalStateException) {
                    errorType = "ConversionError";
                    errorDesc = String.valueOf(t.getCause());
                } else {
                    errorType = "Other";
                    errorDesc = String.valueOf(t.getLocalizedMessage());
                }
                Toast.makeText(DetailsActivity.this, errorDesc, Toast.LENGTH_LONG).show();
                if(getSupportActionBar() != null) {
                    // TODO: some cool display to tell u how wrong u are
                    getSupportActionBar().setTitle(errorType);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        });
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            // TODO: some cool display to tell u how wrong u are
            getSupportActionBar().setTitle("Weather Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
