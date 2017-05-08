package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    // weather condition ID
    @SerializedName("id")
    @Expose
    private Integer id;

    // group of weather params: rain, snow, extreme, etc.
    @SerializedName("main")
    @Expose
    private String main;

    // weather condition within group
    @SerializedName("description")
    @Expose
    private String description;

    // weather icon id
    @SerializedName("icon")
    @Expose
    private String icon;

    public Integer getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        // to capitalize the string -- weird that OW doesn't give you in this form already
        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    public String getIcon() {
        return icon;
    }
}