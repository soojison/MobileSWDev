package io.github.soojison.andwallet.data;

import java.text.DecimalFormat;

public class Summary {
    // singleton model
    private double income;
    private double expenses;
    private static final DecimalFormat df = new DecimalFormat("###.##");

    private static Summary instance = null;

    private Summary() {
        income = 0;
        expenses = 0;
    }

    public static Summary getInstance() {
        if(instance == null) {
            instance = new Summary();
        }
        return instance;
    }

    public void addIncome(double in) {
        income += in;
    }

    public void addExpenses(double out) {
        expenses += out;
    }

    public String getIncome() {
        return df.format(income);
    }

    public String getExpenses() {
        return df.format(expenses);
    }

    public String getBalance() {
        return df.format(income - expenses);
    }

    public void reset() {
        income = 0;
        expenses = 0;
    }
}
