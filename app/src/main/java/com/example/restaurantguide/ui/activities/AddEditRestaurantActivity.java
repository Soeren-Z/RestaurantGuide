package com.example.restaurantguide.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RatingBar;
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
import com.example.restaurantguide.models.RestaurantTagCrossRef;
import com.example.restaurantguide.models.RestaurantWithTags;
import com.example.restaurantguide.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class AddEditRestaurantActivity extends AppCompatActivity {
    private AppDatabase db;
    private EditText name;
    private EditText address;
    private EditText phone;
    private MultiAutoCompleteTextView tagDropDown;
    private RatingBar rating;
    private EditText description;
    private long restaurantId = -1;

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
        restaurantId = getIntent().getLongExtra("restaurant_id", -1);
        db = AppDatabase.getInstance(this);

        name = findViewById(R.id.nameEditText);
        address = findViewById(R.id.addressEditText);
        phone = findViewById(R.id.phoneEditText);
        tagDropDown = findViewById(R.id.tagDropDown);
        rating = findViewById(R.id.ratingBar);
        description = findViewById(R.id.descriptionEditText);

        loadTagsIntoDropDown();


        if (restaurantId != -1) {
            new Thread(() -> {
                RestaurantWithTags restaurantWithTags = db.restaurantDao().getRestaurantWithTags(restaurantId).blockingGet();
                if (restaurantWithTags != null) {
                    runOnUiThread(() -> {
                        Restaurant r = restaurantWithTags.getRestaurant();
                        name.setText(r.getName());
                        address.setText(r.getAddress());
                        phone.setText(r.getPhone());
                        rating.setRating(r.getRating());
                        description.setText(r.getDescription());

                        // Convert tags list to comma-separated string for multi-select input
                        List<Tag> tags = restaurantWithTags.getTags();
                        StringBuilder tagStr = new StringBuilder();
                        for (int i = 0; i < tags.size(); i++) {
                            tagStr.append(tags.get(i).getTagName());
                            if (i < tags.size() - 1) tagStr.append(", ");
                        }
                        tagDropDown.setText(tagStr.toString());
                    });
                }
            }).start();
        }
    }
    private void loadTagsIntoDropDown() {
        new Thread(() -> {
            List<Tag> allTags = db.tagDao().getAllTags().getValue();
            List<String> tagNames = new ArrayList<>();
            if(allTags != null) {
                for (Tag t : allTags) {
                    tagNames.add(t.getTagName());
                }
            }
            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        AddEditRestaurantActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        tagNames
                );
                tagDropDown.setAdapter(adapter);
                tagDropDown.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
                tagDropDown.setThreshold(1);
            });
        }).start();
    }
    public void saveRestaurant(View view) {
        String restaurantName = name.getText().toString().trim();
        String restaurantAddress = address.getText().toString().trim();
        String restaurantPhone = phone.getText().toString().trim();
        String tagInput = tagDropDown.getText().toString().trim();
        float restaurantRating = rating.getRating();
        String restaurantDescription = description.getText().toString().trim();

        new Thread(() -> {
            if(restaurantId == -1) {
                Restaurant restaurant = new Restaurant(restaurantName, restaurantAddress, restaurantPhone, restaurantDescription, restaurantRating);
                restaurantId = db.restaurantDao().insertRestaurant(restaurant);
            } else {
                Restaurant restaurant = db.restaurantDao().getRestaurantByIdDirect(restaurantId);
                restaurant.setName(restaurantName);
                restaurant.setAddress(restaurantAddress);
                restaurant.setPhone(restaurantPhone);
                restaurant.setRating(restaurantRating);
                restaurant.setDescription(restaurantDescription);
                db.restaurantDao().update(restaurant);
            }

            db.restaurantDao().deleteRestaurantTagCrossRef(restaurantId);

            if(!tagInput.isEmpty()) {
                String[] selectedTags = tagInput.split(",");
                for(String tagName : selectedTags) {
                    tagName= tagName.trim();
                    if(tagName.isEmpty()) {
                        continue;
                    }
                    Tag tag = db.tagDao().findByNameDirect(tagName);
                    long tagId;
                    if(tag == null) {
                        Tag newTag = new Tag(tagName);
                        tagId = db.tagDao().insertTag(newTag);
                    } else {
                        tagId = tag.getTagId();
                    }
                    RestaurantTagCrossRef ref = new RestaurantTagCrossRef(restaurantId, tagId);
                    db.restaurantDao().insertRestaurantTagCrossRef(ref);
                }
            }
            runOnUiThread(() -> {
                Toast.makeText(this, "Restaurant Saved", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
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
    }
}