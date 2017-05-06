package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    // weather condition ID
    @SerializedName("id")
    @Expose
    public Integer id;

    // group of weather params: rain, snow, extreme, etc.
    @SerializedName("main")
    @Expose
    public String main;

    // weather condition within group
    @SerializedName("description")
    @Expose
    public String description;

    // weather icon id
    @SerializedName("icon")
    @Expose
    public String icon;

    public Integer getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}