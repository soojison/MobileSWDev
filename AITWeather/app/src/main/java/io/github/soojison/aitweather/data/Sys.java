package io.github.soojison.aitweather.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Mi ez? Nem tudom...
public class Sys {

    @SerializedName("type")
    @Expose
    public Integer type;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("message")
    @Expose
    public Double message;

    // country code: GB, JP, etc.
    @SerializedName("country")
    @Expose
    public String country;

    // sunrise time UNIX, UTC
    @SerializedName("sunrise")
    @Expose
    public Integer sunrise;
    // sunset time UNIX, UTC
    @SerializedName("sunset")
    @Expose
    public Integer sunset;

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