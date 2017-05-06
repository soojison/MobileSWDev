package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    // Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
    @SerializedName("temp")
    @Expose
    private Double temp;

    // pressure in hPa
    @SerializedName("pressure")
    @Expose
    private Double pressure;

    // in percentage
    @SerializedName("humidity")
    @Expose
    private Double humidity;

    // TODO: use these?
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;

    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    public Double getTemp() {
        return temp;
    }

    public Double getPressure() {
        return pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

}