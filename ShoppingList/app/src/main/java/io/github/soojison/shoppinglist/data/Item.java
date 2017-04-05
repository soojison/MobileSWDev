package io.github.soojison.shoppinglist.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class Item extends RealmObject implements Parcelable {

    public static final String INDEX = "index";
    private String name;
    private String description;
    private double price;
    private boolean done;
    private int category;
    private int index;

    public Item() {

    }

    public Item(String name, String description, double price, boolean done, int category, int index) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.done = done;
        this.category = category;
        this.index = index;
    }

    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        done = in.readByte() != 0;
        category = in.readInt();
        index = in.readInt();
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeByte((byte) (done ? 1 : 0));
        dest.writeInt(category);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
