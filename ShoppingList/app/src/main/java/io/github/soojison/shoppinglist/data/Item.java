package io.github.soojison.shoppinglist.data;

public class Item {

    public String name;
    public String description;
    public double price;
    public boolean boughtStatus;
    public short category;

    public Item() {

    }

    public Item(String name, String description, double price, boolean boughtStatus, short category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.boughtStatus = boughtStatus;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isBoughtStatus() {
        return boughtStatus;
    }

    public void setBoughtStatus(boolean boughtStatus) {
        this.boughtStatus = boughtStatus;
    }

    public short getCategory() {
        return category;
    }

    public void setCategory(short category) {
        this.category = category;
    }
}
