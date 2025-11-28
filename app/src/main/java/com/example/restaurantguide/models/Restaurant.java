package com.example.restaurantguide.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "restaurants")
public class Restaurant {
    @PrimaryKey(autoGenerate = true)
    private long restaurantId;
    private String name;
    private String address;
    private String phone;
    private String description;
    private float rating;
    public Restaurant(String name, String address, String phone, String description, float rating) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.rating = rating;
    }
    public long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(long id) {
        this.restaurantId = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
}
