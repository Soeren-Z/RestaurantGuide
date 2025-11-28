package com.example.restaurantguide.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.restaurantguide.R;
import com.example.restaurantguide.db.AppDatabase;
import com.example.restaurantguide.models.Restaurant;
import com.example.restaurantguide.models.RestaurantWithTags;
import com.example.restaurantguide.models.Tag;

public class RestaurantDetailsActivity extends AppCompatActivity {
    private AppDatabase db;
    private TextView name;
    private TextView address;
    private TextView phone;
    private TextView description;
    private RatingBar rating;

    private Button shareButton;
    private Button viewMapButton;
    private Button editButton;
    private Button deleteButton;

    private LinearLayout tagsContainer;
    private ImageView restaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_details);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = AppDatabase.getInstance(this);
        // XML connections
        restaurantImage = findViewById(R.id.restaurantImageView);
        name = findViewById(R.id.nameTextView);
        address = findViewById(R.id.addressTextView);
        phone = findViewById(R.id.phoneTextView);
        description = findViewById(R.id.descriptionTextView);
        rating = findViewById(R.id.ratingBar);

        shareButton = findViewById(R.id.shareButton);
        viewMapButton = findViewById(R.id.viewMapButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);

        tagsContainer = findViewById(R.id.tagsContainer);

        // Load restaurant details
        long restaurantId = getIntent().getLongExtra("restaurant_id", -1);

        if(restaurantId != -1) {
            //Executor executor = Executors.newSingleThreadExecutor();
            new Thread(() -> {
                RestaurantWithTags restaurant = db.restaurantDao().getRestaurantWithTags(restaurantId);

                if (restaurant != null) {
                    runOnUiThread(() -> {
                        name.setText(restaurant.getRestaurant().getName());
                        address.setText(restaurant.getRestaurant().getAddress());
                        phone.setText(restaurant.getRestaurant().getPhone());
                        description.setText(restaurant.getRestaurant().getDescription());
                        rating.setRating(restaurant.getRestaurant().getRating());

                        // ---------- TAG CHIPS ----------
                        tagsContainer.removeAllViews();
                        for(Tag tag : restaurant.getTags()) {
                            TextView chip = new TextView(this);
                            chip.setText(tag.getTagName());
                            chip.setPadding(24, 12, 24, 12);
                            chip.setBackgroundResource(R.drawable.chip_background);
                            chip.setTextColor(Color.BLACK);
                            chip.setTextSize(14f);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(12, 12, 12, 12);
                            chip.setLayoutParams(params);

                            tagsContainer.addView(chip);
                        }
                        // Optional image
                        // restaurantImage.setImageResource(R.drawable.ic_image_placeholder);
                    });
                }
            }).start();
        }

        // SHARE BUTTON
        shareButton.setOnClickListener(v -> {
            String text = "Check out this restaurant:\n\n"
                    + "Name: " + name.getText() + "\n"
                    + "Address: " + address.getText() + "\n"
                    + "Phone: " + phone.getText() + "\n"
                    + "Rating: " + rating.getRating() + " stars\n"
                    + "Description: " + description.getText();

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, text);
            sendIntent.setType("text/plain");

            startActivity(Intent.createChooser(sendIntent, "Share Restaurant"));
        });

        // MAP BUTTON
        viewMapButton.setOnClickListener(v -> {
            String addr = address.getText().toString();
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(addr));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        // EDIT BUTTON
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditRestaurantActivity.class);
            intent.putExtra("restaurant_id", restaurantId);
            startActivity(intent);
        });
    }
    public void deleteRestaurant(View view) {
        new Thread(() -> {
            long restaurantId = getIntent().getLongExtra("restaurant_id", -1);
            if(restaurantId != -1) {
                Restaurant toDelete = db.restaurantDao().getRestaurantById(restaurantId);
                if(toDelete != null) {
                    db.restaurantDao().delete(toDelete);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Restaurant deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Restaurant not found", Toast.LENGTH_SHORT).show());
                }
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.team_info_activity) {
            Intent intent = new Intent(this, TeamInfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
