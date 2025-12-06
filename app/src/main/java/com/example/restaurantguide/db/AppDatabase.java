package com.example.restaurantguide.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.restaurantguide.daos.RestaurantDao;
import com.example.restaurantguide.daos.TagDao;
import com.example.restaurantguide.models.Restaurant;
import com.example.restaurantguide.models.RestaurantTagCrossRef;
import com.example.restaurantguide.models.Tag;

@Database(entities = {Restaurant.class, Tag.class, RestaurantTagCrossRef.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract RestaurantDao restaurantDao();
    public abstract TagDao tagDao();
    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "restaurants_database"
            ).build();
        }
        return INSTANCE;
    }
}
