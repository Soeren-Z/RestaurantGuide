package com.example.restaurantguide.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import com.example.restaurantguide.models.Restaurant;
import com.example.restaurantguide.models.RestaurantTagCrossRef;
import com.example.restaurantguide.models.RestaurantWithTags;

import java.util.List;

@Dao
public interface RestaurantDao {
    @Insert
    long insertRestaurant(Restaurant restaurant);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertRestaurantTagCrossRef(RestaurantTagCrossRef crossRef);

    @Update
    void update(Restaurant restaurant);

    @Transaction
    @Query("SELECT * FROM restaurants")
    LiveData<List<RestaurantWithTags>> getAllRestaurantWithTags();

    @Transaction
    @Query("SELECT * FROM restaurants WHERE restaurantId = :restaurantId")
    Single<RestaurantWithTags> getRestaurantWithTags(long restaurantId);

    @Query("SELECT * FROM restaurants WHERE restaurantId = :restaurantId")
    Single<Restaurant> getRestaurantById(long restaurantId);
    @Delete
    void delete(Restaurant restaurant);

    @Query("DELETE FROM RestaurantTagCrossRef WHERE restaurantId = :restaurantId")
    Completable deleteRestaurantTagCrossRef(long restaurantId);

    @Query("SELECT * FROM restaurants WHERE restaurantId = :restaurantId LIMIT 1")
    Restaurant getRestaurantByIdDirect(long restaurantId);

    @Transaction
    @Query("SELECT DISTINCT r.* FROM restaurants r " +
           "LEFT JOIN RestaurantTagCrossRef rtc ON r.restaurantId = rtc.restaurantId " +
           "LEFT JOIN Tag t ON rtc.tagId = t.tagId " +
           "WHERE r.name LIKE '%' || :query || '%' " +
           "OR t.tagName LIKE '%' || :query || '%'")
    LiveData<List<RestaurantWithTags>> searchRestaurantsWithTags(String query);
}
