package com.example.restaurantguide;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    List<Restaurant> getAll();

    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    Restaurant getRestaurantById(int restaurantId);

    @Insert
    void insert(Restaurant restaurant);

    @Delete
    void delete(Restaurant restaurant);
}
