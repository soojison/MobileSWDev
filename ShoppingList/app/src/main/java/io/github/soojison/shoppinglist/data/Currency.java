package io.github.soojison.shoppinglist.data;

public class Currency {

    private static Currency instance = null;
    private String currency = "$"; //as default

    private Currency() {

    }

    public static Currency getInstance() {
        if (instance == null) {
            instance = new Currency();
        }
        return instance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
