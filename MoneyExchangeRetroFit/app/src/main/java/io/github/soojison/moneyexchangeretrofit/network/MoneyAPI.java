package io.github.soojison.moneyexchangeretrofit.network;

import io.github.soojison.moneyexchangeretrofit.data.MoneyResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoneyAPI {

    // http get method
    // "latest" and "base" should match the actual API url
    // call (= network call) should return money result obj
    // if multiple query, then add another parameter to the func sig
    // (make sure you don't have a typo!
    // because response format was not in the format that we wanted so it will throw a null pointer exception
    @GET("latest")
    Call<MoneyResult> getRatesForUSD(@Query("base") String base);
}
