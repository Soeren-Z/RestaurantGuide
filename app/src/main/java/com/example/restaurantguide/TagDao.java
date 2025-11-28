package com.example.restaurantguide;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insertTag(Tag tag);

    @Query("SELECT * FROM Tag WHERE tagName = :name LIMIT 1")
    Tag findByName(String name);

    @Query("SELECT * FROM Tag")
    List<Tag> getAllTags();
}
