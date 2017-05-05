package io.github.soojison.aitweather.realm;

import io.realm.RealmObject;

public class RealmString extends RealmObject {
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
