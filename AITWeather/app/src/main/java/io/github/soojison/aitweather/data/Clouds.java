package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Clouds {

    // cloudiness in %
    @SerializedName("all")
    @Expose
    private Double all;

    public Double getAll() {
        return all;
    }
}