package io.github.soojison.andwallet.data;

public class Summary {
    // singleton model
    private double income;
    private double expenses;

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

    public double getIncome() {
        return income;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getbalance() {
        return income - expenses;
    }

    public void reset() {
        income = 0;
        expenses = 0;
    }
}
