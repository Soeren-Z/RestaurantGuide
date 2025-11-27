package com.example.restaurantguide;

import java.util.List;

public class Restaurant {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private List<String> tags;
    private float rating;

    // Constructor with ID - for existing restaurants from database
    public Restaurant(int id, String name, String address, String phone, String description, List<String> tags, float rating) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.rating = rating;
    }

    // Constructor without ID - for new restaurants to be added to database
    public Restaurant(String name, String address, String phone, String description, List<String> tags, float rating) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.tags = tags;
        this.rating = rating;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public float getRating() {
        return rating;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // Helper method to get tags as comma-separated string for database storage
    public String getTagsAsString() {
        if (tags == null || tags.isEmpty()) {
            return "";
        }
        return String.join(", ", tags);
    }

    // Helper method to set tags from comma-separated string from database
    public static List<String> parseTagsFromString(String tagsString) {
        if (tagsString == null || tagsString.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        String[] tagArray = tagsString.split(",");
        java.util.ArrayList<String> tagList = new java.util.ArrayList<>();
        for (String tag : tagArray) {
            tagList.add(tag.trim());
        }
        return tagList;
    }
}