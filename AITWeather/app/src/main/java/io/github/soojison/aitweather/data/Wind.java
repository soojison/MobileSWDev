package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wind {

    // speed in unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    @SerializedName("speed")
    @Expose
    public Double speed;

    // degrees (meteorological)
    @SerializedName("deg")
    @Expose
    public Integer deg;

    public Double getSpeed() {
        return speed;
    }

    public Integer getDeg() {
        return deg;
    }

}