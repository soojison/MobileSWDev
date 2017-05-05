package io.github.soojison.aitweather.api;

import io.github.soojison.aitweather.data.WeatherResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    @GET("data/2.5/weather")
    Call<WeatherResult> getCurrentWeather(@Query("q") String city,
                                          @Query("units") String units,
                                          @Query("appid") String appid);
}
