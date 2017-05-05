package io.github.soojison.aitweather.api;

import io.github.soojison.aitweather.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {


    String WEATHER_API_PATH = "data/2.5/weather";
    String QUERY_CITY = "q";
    String QUERY_UNITS = "units";
    String QUERY_API_KEY = "appid";

    @GET(WEATHER_API_PATH)
    Call<WeatherResult> getCurrentWeather(@Query(QUERY_CITY) String city,
                                          @Query(QUERY_UNITS) String units,
                                          @Query(QUERY_API_KEY) String apiKey);
}
