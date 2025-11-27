package com.example.restaurantguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RestaurantStorage {

    // Set variables for database helper and database instance
    private RestaurantDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    // Constructor for RestaurantStorage
    public RestaurantStorage(Context context) {
        dbHelper = new RestaurantDatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Adding a new restaurant to the database
    public long addRestaurant(Restaurant r) {
        // SQL INSERT statement with placeholders for values
        String sql = "INSERT INTO restaurants (name, address, phone, tags, rating, description) VALUES (?, ?, ?, ?, ?, ?)";

        // Execute the SQL with insert values replacing the placeholders
        database.execSQL(sql, new Object[]{
                r.getName(),
                r.getAddress(),
                r.getPhone(),
                r.getTagsAsString(),  // Convert List<String> to comma-separated string
                r.getRating(),
                r.getDescription()
        });

        // Retrieve the auto-generated ID of the most recently inserted row
        Cursor cursor = database.rawQuery("SELECT last_insert_rowid()", null);
        cursor.moveToFirst();
        long id = cursor.getLong(0);
        cursor.close();

        // Set the retrieved ID to the restaurant object
        r.setId((int) id);
        return id;
    }

    // Get a specific restaurant by its ID
    public Restaurant getRestaurant(int id) {
        String sql = "SELECT * FROM restaurants WHERE id = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(id)});

        Restaurant restaurant = null;
        if (cursor.moveToFirst()) {
            restaurant = cursorToRestaurant(cursor);
        }
        cursor.close();
        return restaurant;
    }

    // Get all restaurants from the database sorted by name alphabetically
    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurants ORDER BY name ASC";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            restaurants.add(cursorToRestaurant(cursor));
        }
        cursor.close();
        return restaurants;
    }

    // Update an existing restaurant's details
    public void updateRestaurant(Restaurant r) {
        String sql = "UPDATE restaurants SET name = ?, address = ?, phone = ?, tags = ?, rating = ?, description = ? WHERE id = ?";

        database.execSQL(sql, new Object[]{
                r.getName(),
                r.getAddress(),
                r.getPhone(),
                r.getTagsAsString(),  // Convert List<String> to comma-separated string
                r.getRating(),
                r.getDescription(),
                r.getId()
        });
    }

    // Delete a restaurant from the database
    public void deleteRestaurant(int id) {
        String sql = "DELETE FROM restaurants WHERE id = ?";
        database.execSQL(sql, new Object[]{id});
    }

    // Search for restaurants by name
    public List<Restaurant> searchRestaurants(String query) {
        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT * FROM restaurants WHERE name LIKE ? ORDER BY name ASC";
        Cursor cursor = database.rawQuery(sql, new String[]{"%" + query + "%"});

        while (cursor.moveToNext()) {
            restaurants.add(cursorToRestaurant(cursor));
        }
        cursor.close();
        return restaurants;
    }

    // Helper method to convert a database row (Cursor) into a Restaurant object
    private Restaurant cursorToRestaurant(Cursor cursor) {
        int id = cursor.getInt(0);
        String name = cursor.getString(1);
        String address = cursor.getString(2);
        String phone = cursor.getString(3);
        String tagsString = cursor.getString(4);
        float rating = cursor.getFloat(5);
        String description = cursor.getString(6);

        // Parse tags from comma-separated string to List<String>
        List<String> tags = Restaurant.parseTagsFromString(tagsString);

        return new Restaurant(id, name, address, phone, description, tags, rating);
    }

    // Method to close the database connection
    public void close() {
        dbHelper.close();
    }
}