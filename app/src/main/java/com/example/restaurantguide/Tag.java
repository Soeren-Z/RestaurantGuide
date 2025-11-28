package com.example.restaurantguide;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tag {
    @PrimaryKey(autoGenerate = true)
    private long tagId;
    private String tagName;
    public Tag(String tagName) {
        this.tagName = tagName;
    }
    public long getTagId() {
        return tagId;
    }
    public void setTagId(long tagId) {
        this.tagId = tagId;
    }
    public String getTagName() {
        return tagName;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
