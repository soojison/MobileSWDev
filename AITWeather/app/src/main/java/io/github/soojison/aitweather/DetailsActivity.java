package io.github.soojison.aitweather;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String KEY_CITY_NAME = "KEY_CITY_NAME";

    private static final String HTTP_API_OPENWEATHERMAP_ORG = "http://api.openweathermap.org";
    private static final String MY_API_KEY = "13ecf6e64cda416ef869f94f53a99417";
    private static final String API_IMAGE_BASEURL = "http://openweathermap.org/img/w/";

    private static ProgressDialog mProgressDialog;

    private WeatherApi weatherApi;

    private boolean units; // true = metric, false = imperial

    @BindView(R.id.tvCurrentTemp) TextView tvCurrentTemp;
    @BindView(R.id.imgWeatherIcon) ImageView imgWeatherIcon;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.tvWindSpeed) TextView tvWindSpeed;
    @BindView(R.id.tvWindDir) TextView tvWindDir;
    @BindView(R.id.tvCloudPercent) TextView tvCloudPercent;
    @BindView(R.id.tvCloudDesc) TextView tvCloudDesc;
    @BindView(R.id.tvPressure) TextView tvPressure;
    @BindView(R.id.tvHumidity) TextView tvHumidity;
    @BindView(R.id.tvSunrise) TextView tvSunrise;
    @BindView(R.id.tvSunset) TextView tvSunSet;
    @BindView(R.id.viewInvalidCity) RelativeLayout viewInvalidCity;
    @BindView(R.id.viewWithWeatherData) LinearLayout viewWithWeatherData;
    @BindView(R.id.viewError) RelativeLayout viewError;
    @BindView(R.id.tvErrorDesc) TextView tvErrorDesc;
    private double longitude = 0;
    private double latitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_loading));

        if(getIntent().hasExtra(KEY_CITY_NAME)) { // if there is no intent we can't do anything
            String cityName = getIntent().getStringExtra(KEY_CITY_NAME);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            units = prefs.getString("temp_list", "0").equals("0");
            // TODO: toolbar menu that lets you refresh the data
            initializeToolbar();
            showDialog();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HTTP_API_OPENWEATHERMAP_ORG)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            weatherApi = retrofit.create(WeatherApi.class);
            getWeatherInfo(cityName);
        }
    }

    private void getWeatherInfo(String cityName) {
        String units_param = units ? "metric" : "imperial";

        try { // URLEncoder requires try/catch block
            String queryCity = URLEncoder.encode(cityName, "UTF-8");
            Call<WeatherResult> call = weatherApi.getCurrentWeather(queryCity, units_param, MY_API_KEY);
            call.enqueue(new Callback<WeatherResult>() {
                @Override
                public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                    hideDialog();
                    if(response.code() == 200) { // if successful
                        populateWeatherData(response.body());
                    } else if(response.code() == 404) { // if city does not exist
                        viewInvalidCity.setVisibility(View.VISIBLE);
                        viewWithWeatherData.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<WeatherResult> call, Throwable t) { // other errors
                    hideDialog();
                    viewError.setVisibility(View.VISIBLE);
                    viewWithWeatherData.setVisibility(View.INVISIBLE);
                    String errorType, errorDesc;
                    if(t instanceof IOException) {
                        errorType = "Timeout Error";
                        errorDesc = String.valueOf(t.getLocalizedMessage());
                    } else if (t instanceof IllegalStateException) {
                        errorType = "Conversion Error";
                        errorDesc = String.valueOf(t.getLocalizedMessage());
                    } else {
                        errorType = "Other Error";
                        errorDesc = String.valueOf(t.getLocalizedMessage());
                    }
                    tvErrorDesc.setText(errorType);
                    Toast.makeText(DetailsActivity.this, errorDesc, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void populateWeatherData(WeatherResult body) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(
                    R.string.weather_info_header, body.getName(), body.getSys().getCountry()));
        }

        String iconURL = API_IMAGE_BASEURL + body.getWeather().get(0).getIcon() + ".png";
        String degrees = units ? "°C" : "°F";
        String speed = units ? "m/s" : "mph";
        // no need to extract the rest since it's universal/won't need translations...
        tvCurrentTemp.setText(body.getMain().getTemp() + degrees);
        tvDescription.setText(body.getWeather().get(0).getDescription());
        Glide.with(getApplicationContext()).load(iconURL).into(imgWeatherIcon);
        tvWindSpeed.setText(body.getWind().getSpeed() + " " + speed);
        tvWindDir.setText(getWindDirection(body.getWind().getDeg()));
        tvCloudPercent.setText(body.getClouds().getAll() + "%");
        tvCloudDesc.setText(getCloudInfo(body.getClouds().getAll()));
        tvPressure.setText(body.getMain().getPressure() + " hPa");
        tvHumidity.setText(body.getMain().getHumidity() + "%");
        tvSunrise.setText(getSunTime(body.getSys().getSunrise()));
        tvSunSet.setText(getSunTime(body.getSys().getSunset()));
        longitude = body.getCoord().getLon();
        latitude = body.getCoord().getLat();
        SupportMapFragment supportMapFragment =  SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
    }

    private String getCloudInfo(Double percent) {
        if (0 == percent) {
            return getString(R.string.cloud_clear_sky);
        } else if(0 < percent && percent <= 10) {
            return getString(R.string.cloud_few_clouds);
        } else if(10 < percent && percent <= 50) {
            return getString(R.string.cloud_scattered_clouds);
        } else if(50 < percent && percent <= 90) {
            return getString(R.string.cloud_broken_clouds);
        } else {
            return getString(R.string.cloud_overcast_clouds);
        }
    }

    private String getSunTime(Integer unixTime) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Date time = new Date((long) unixTime * 1000);
        return dateFormat.format(time);
    }

    private String getWindDirection(Double degree) {
        if(degree == null) { return "undefined"; } // api returns null for wind direction sometimes
        if(11.25 < degree && degree <= 33.75) { return "NNE"; }
        else if(33.75 < degree && degree <= 56.25) { return "NE"; }
        else if(56.25 < degree && degree <= 78.75) { return "ENE"; }
        else if(78.75 < degree && degree <= 101.25) { return "E"; }
        else if(101.25 < degree && degree <= 123.75) { return "ESE"; }
        else if(123.75 < degree && degree <= 146.25) { return "SE"; }
        else if(146.25 < degree && degree <= 168.75) { return "SSE"; }
        else if(168.75 < degree && degree <= 191.25) { return "S"; }
        else if(191.25 < degree && degree <= 213.75) { return "SSW"; }
        else if(213.75 < degree && degree <= 236.25) { return "SW"; }
        else if(236.25 < degree && degree <= 258.75) { return "WSW"; }
        else if(258.75 < degree && degree <= 281.25) { return "W"; }
        else if(213.75 < degree && degree <= 236.25) { return "SW"; }
        else if(236.25 < degree && degree <= 258.75) { return "WSW"; }
        else if(258.75 < degree && degree <= 281.25) { return "W"; }
        else if(281.25 < degree && degree <= 303.75) { return "WNW"; }
        else if(303.75 < degree && degree <= 326.25) { return "NW"; }
        else if(326.25 < degree && degree <= 348.75) { return "NNW"; }
        else { return "N"; }
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.placeholder_activity_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public static void showDialog() {
        if(mProgressDialog != null && !mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public static void hideDialog() {

        if(mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng marker = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions()
                .position(marker)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker,10));
    }
}
