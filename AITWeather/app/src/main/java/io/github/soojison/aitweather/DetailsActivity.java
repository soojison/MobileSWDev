package io.github.soojison.aitweather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.soojison.aitweather.api.WeatherApi;
import io.github.soojison.aitweather.data.WeatherResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {

    public static final String KEY_CITY_NAME = "KEY_CITY_NAME";

    public static final String HTTP_API_OPENWEATHERMAP_ORG = "http://api.openweathermap.org";
    public static final String MY_API_KEY = "13ecf6e64cda416ef869f94f53a99417";
    public static final String API_IMAGE_BASEURL = "http://openweathermap.org/img/w/";

    public WeatherApi weatherApi;

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvCurrentTemp) TextView tvCurrentTemp;
    @BindView(R.id.imgWeatherIcon) ImageView imgWeatherIcon;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvWind) TextView tvWind;
    @BindView(R.id.tvCloud) TextView tvCloud;
    @BindView(R.id.tvPressure) TextView tvPressure;
    @BindView(R.id.tvHumidity) TextView tvHumidity;
    @BindView(R.id.tvSunrise) TextView tvSunrise;
    @BindView(R.id.tvSunset) TextView tvSunSet;


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
            getWeatherInfo(cityName);
        }
    }

    private void getWeatherInfo(String cityName) {
        // todo: handle loading while loading api data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String units = prefs.getString("temp_list", "metric");
        Call<WeatherResult> call = weatherApi.getCurrentWeather(cityName, units, MY_API_KEY);
        call.enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                populateWeatherData(response.body());
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

    private void populateWeatherData(WeatherResult body) {

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(body.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String iconURL = API_IMAGE_BASEURL + body.getWeather().get(0).getIcon() + ".png";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String degrees = "°";
        String speed;
        if(prefs.getString("temp_list", "metric").equals("metric")) { // metric
            degrees = degrees.concat("C");
            speed = "m/s";
        } else { // imperial
            degrees = degrees.concat("F");
            speed = "m/h";
        }


        // TODO: Extract string
        tvTitle.setText("Weather in " + body.getName() + ", " + body.getSys().getCountry());
        tvCurrentTemp.setText(body.getMain().getTemp() + degrees);
        tvDescription.setText(body.getWeather().get(0).getDescription());
        Glide.with(getApplicationContext()).load(iconURL).into(imgWeatherIcon);
        tvWind.setText(body.getWind().getSpeed() + speed + " in the direction of "
                + getWindDirection(body.getWind().getDeg()));
        tvCloud.setText(getCloudInfo(body.getClouds().getAll()));
        tvPressure.setText(body.getMain().getPressure() + "hPa");
        tvHumidity.setText(body.getMain().getHumidity() + "%");

        tvSunrise.setText(getSunTime(body.getSys().getSunrise()));
        tvSunSet.setText(getSunTime(body.getSys().getSunset()));
    }

    // TODO: String extraction
    private String getCloudInfo(Double percent) {
        if(0 < percent && percent <= 10) {
            return "Few clouds";
        } else if(10 < percent && percent <= 50) {
            return "Scattered clouds";
        } else if(50 < percent && percent <= 90) {
            return "Broken clouds";
        } else {
            return "Overcast clouds";
        }
    }


    private String getSunTime(Integer unixTime) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date time = new Date((long) unixTime * 1000);
        return dateFormat.format(time);
    }

    private String getWindDirection(Integer degree) {
        if(11.25 < degree && degree <= 33.75) { return "North-northeast"; }
        if(33.75 < degree && degree <= 56.25) { return "Northeast"; }
        if(56.25 < degree && degree <= 78.75) { return "East-northeast"; }
        if(78.75 < degree && degree <= 101.25) { return "East"; }
        if(101.25 < degree && degree <= 123.75) { return "East-Southeast"; }
        if(123.75 < degree && degree <= 146.25) { return "Southeast"; }
        if(146.25 < degree && degree <= 168.75) { return "South-Southeast"; }
        if(168.75 < degree && degree <= 191.25) { return "South"; }
        if(191.25 < degree && degree <= 213.75) { return "South-Southwest"; }
        if(213.75 < degree && degree <= 236.25) { return "Southwest"; }
        if(236.25 < degree && degree <= 258.75) { return "West-southwest"; }
        if(258.75 < degree && degree <= 281.25) { return "West"; }
        if(213.75 < degree && degree <= 236.25) { return "Southwest"; }
        if(236.25 < degree && degree <= 258.75) { return "West-southwest"; }
        if(258.75 < degree && degree <= 281.25) { return "West"; }
        if(281.25 < degree && degree <= 303.75) { return "West-northwest"; }
        if(303.75 < degree && degree <= 326.25) { return "Northwest"; }
        if(326.25 < degree && degree <= 348.75) { return "North-northwest"; }
        return "North";
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
