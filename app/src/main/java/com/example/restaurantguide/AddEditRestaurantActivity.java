package com.example.restaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AddEditRestaurantActivity extends AppCompatActivity {

    // Declaring variables
    private EditText nameEditText, addressEditText, phoneEditText, tagsEditText, descriptionEditText;
    private RatingBar ratingBar;
    private Button saveButton;
    private RestaurantStorage storage;

    // Variable we use to check if we are editing an existing restaurant or adding a new one
    private int restaurantId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_restaurant);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize storage handler
        storage = new RestaurantStorage(this);

        // Initialize views by their IDs from the layout file
        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        ratingBar = findViewById(R.id.ratingBar);
        saveButton = findViewById(R.id.saveButton);

        // Set it so that the ratingBar increments by 0.5 stars
        ratingBar.setStepSize(0.5f);

        // Check if we're editing an existing restaurant or adding a new one
        restaurantId = getIntent().getIntExtra("RESTAURANT_ID", -1);

        // If the restaurant ID is NOT -1, we are in edit mode
        if (restaurantId != -1) {
            loadRestaurantData();
        }

        // Click listener for save button -> calls saveRestaurant method
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurant();
            }
        });
    }

    // Method to load existing restaurant data into the edit input fields
    private void loadRestaurantData() {
        Restaurant restaurant = storage.getRestaurant(restaurantId);
        if (restaurant != null) {
            nameEditText.setText(restaurant.getName());
            addressEditText.setText(restaurant.getAddress());
            phoneEditText.setText(restaurant.getPhone());
            tagsEditText.setText(restaurant.getTagsAsString());
            ratingBar.setRating(restaurant.getRating());
            descriptionEditText.setText(restaurant.getDescription());
        }
    }

    // Method to save the restaurant data
    private void saveRestaurant() {
        // Get data from fields
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String tagsString = tagsEditText.getText().toString().trim();
        float rating = ratingBar.getRating();
        String description = descriptionEditText.getText().toString().trim();

        // Basic validation to ensure the name field is not empty
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a restaurant name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse tags from string to List
        List<String> tags = Restaurant.parseTagsFromString(tagsString);

        // Check if we're in edit mode or add mode
        if (restaurantId != -1) {
            // Update existing restaurant
            Restaurant restaurant = new Restaurant(restaurantId, name, address, phone, description, tags, rating);
            storage.updateRestaurant(restaurant);
            Toast.makeText(this, "Restaurant updated", Toast.LENGTH_SHORT).show();
        } else {
            // Add new restaurant
            Restaurant restaurant = new Restaurant(name, address, phone, description, tags, rating);
            storage.addRestaurant(restaurant);
            Toast.makeText(this, "Restaurant added", Toast.LENGTH_SHORT).show();
        }

        // Close the activity and return to the previous screen
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        if(item.getItemId() == R.id.team_info_activity) {
            Intent intent = new Intent(this, TeamInfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection
        if (storage != null) {
            storage.close();
        }
    }
}