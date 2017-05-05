package io.github.soojison.aitweather.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmString extends RealmObject {

    @PrimaryKey
    private String cityID;
    private String cityName;

    public RealmString() {

    }

    public RealmString(String cityID, String cityName) {
        this.cityID = cityID;
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }
}
