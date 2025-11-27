package com.example.restaurantguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestaurantDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version constants
    private static final String DATABASE_NAME = "restaurantGuide.db";
    private static final int DATABASE_VERSION = 1;

    // Table name for restaurants
    public static final String TABLE_RESTAURANTS = "restaurants";

    // Column names for the restaurants table
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DESCRIPTION = "description";

    // SQL statement to create the restaurants table
    private static final String CREATE_TABLE_RESTAURANTS =
            "CREATE TABLE " + TABLE_RESTAURANTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_TAGS + " TEXT, " +
                    COLUMN_RATING + " REAL, " +
                    COLUMN_DESCRIPTION + " TEXT" +
                    ");";

    // Constructor for the database helper
    public RestaurantDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESTAURANTS);
    }

    // Here because it's required, but unlikely that we will actually use this method
    // Just made it so that if we do "upgrade" the database, we drop the entirety of the old table and create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        onCreate(db);
    }
}