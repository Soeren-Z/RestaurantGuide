package com.example.restaurantguide.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.restaurantguide.models.Tag;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface TagDao {
    @Insert
    long insertTag(Tag tag);

    @Query("SELECT * FROM Tag WHERE tagName = :name LIMIT 1")
    Single<Tag> findByName(String name);

    @Query("SELECT * FROM Tag")
    LiveData<List<Tag>> getAllTags();

    @Query("SELECT * FROM Tag")
    List<Tag> getAllTagsDirect();

    @Query("SELECT * FROM tag WHERE tagName = :name LIMIT 1")
    Tag findByNameDirect(String name);
}
