package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sys {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("id")
    @Expose
    private Integer id;
    // Mi ez? Nem tudom...
    @SerializedName("message")
    @Expose
    private Double message;

    // country code: GB, JP, etc.
    @SerializedName("country")
    @Expose
    private String country;

    // sunrise time UNIX, UTC
    @SerializedName("sunrise")
    @Expose
    private Integer sunrise;

    // sunset time UNIX, UTC
    @SerializedName("sunset")
    @Expose
    private Integer sunset;

    public Integer getType() {
        return type;
    }

    public Integer getId() {
        return id;
    }

    public Double getMessage() {
        return message;
    }

    public String getCountry() {
        return country;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }
}