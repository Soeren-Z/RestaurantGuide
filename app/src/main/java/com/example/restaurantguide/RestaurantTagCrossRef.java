package com.example.restaurantguide;

import androidx.room.Entity;

@Entity(primaryKeys = {"restaurantId", "tagId"})
public class RestaurantTagCrossRef {
    private long restaurantId;
    private long tagId;

    public RestaurantTagCrossRef(long restaurantId, long tagId) {
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
}
