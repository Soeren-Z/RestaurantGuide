package com.example.restaurantguide;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RestaurantWithTags {
    @Embedded
    private Restaurant restaurant;

    @Relation(
            parentColumn = "restaurantId",
            entityColumn = "tagId",
            associateBy = @Junction(RestaurantTagCrossRef.class)
    )
    private List<Tag> tags;

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
