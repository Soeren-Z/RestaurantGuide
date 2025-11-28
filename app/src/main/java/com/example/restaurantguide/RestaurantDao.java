package com.example.restaurantguide;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert
    long insertRestaurant(Restaurant restaurant);
    @Insert
    void insertRestaurantTagCrossRef(RestaurantTagCrossRef crossRef);

    @Transaction
    @Query("SELECT * FROM restaurants")
    List<RestaurantWithTags> getAllRestaurantWithTags();

    @Transaction
    @Query("SELECT * FROM restaurants WHERE restaurantId = :restaurantId")
    RestaurantWithTags getRestaurantWithTags(long restaurantId);

    @Delete
    void delete(Restaurant restaurant);

}
