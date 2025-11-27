package com.example.restaurantguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RestaurantDetailsActivity extends AppCompatActivity {

    // Declare variables
    private TextView nameTextView, addressTextView, phoneTextView, tagsTextView, descriptionTextView;
    private RatingBar ratingBar;
    private Button viewMapButton, shareButton, editButton, deleteButton;
    private RestaurantStorage storage;
    private int restaurantId;
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_details);

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

        // Initialize views by their IDs from layout file
        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        tagsTextView = findViewById(R.id.tagsTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        ratingBar = findViewById(R.id.ratingBar);
        viewMapButton = findViewById(R.id.viewMapButton);
        shareButton = findViewById(R.id.shareButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Get restaurant ID from intent extras
        restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        if (restaurantId != -1) {
            loadRestaurantDetails();
        }

        // Click listener for edit button -> sends you to AddEditRestaurantActivity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, AddEditRestaurantActivity.class);
                intent.putExtra("RESTAURANT_ID", restaurantId);
                startActivity(intent);
            }
        });

        // Click listener for delete button -> shows confirmation dialog before deleting
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

        // Click listener for share button -> opens share intent
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareRestaurant();
            }
        });

        // Click listener for view map button -> opens MapsActivity using restaurant ID
        viewMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestaurantDetailsActivity.this, MapsActivity.class);
                intent.putExtra("RESTAURANT_ID", restaurantId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload restaurant details when returning to activity
        if (restaurantId != -1) {
            loadRestaurantDetails();
        }
    }

    // Method to load restaurant details from storage and display them in the views
    private void loadRestaurantDetails() {
        restaurant = storage.getRestaurant(restaurantId);

        if (restaurant != null) {
            nameTextView.setText(restaurant.getName());
            addressTextView.setText(restaurant.getAddress());
            phoneTextView.setText(restaurant.getPhone());
            tagsTextView.setText(restaurant.getTagsAsString());
            descriptionTextView.setText(restaurant.getDescription());
            ratingBar.setRating(restaurant.getRating());
        }
    }

    // Method that shows confirmation dialog before deleting restaurant
    private void showDeleteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Restaurant")
                .setMessage("Are you sure you want to delete this restaurant?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        storage.deleteRestaurant(restaurantId);
                        Toast.makeText(RestaurantDetailsActivity.this, "Restaurant deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Method to share restaurant details via text
    private void shareRestaurant() {
        if (restaurant != null) {
            String shareText =
                    "Restaurant: " + restaurant.getName() + "\n"
                            + "Rating: " + restaurant.getRating() + "/5\n"
                            + "Address: " + restaurant.getAddress() + "\n"
                            + "Phone: " + restaurant.getPhone() + "\n"
                            + "Tags: " + restaurant.getTagsAsString() + "\n\n"
                            + "Description: " + restaurant.getDescription();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Restaurant"));
        }
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