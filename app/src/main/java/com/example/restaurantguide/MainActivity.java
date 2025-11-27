package com.example.restaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RestaurantAdapter adapter;
    private ListView listview;
    private EditText searchBar;
    private RestaurantStorage storage;
    private ArrayList<Restaurant> restaurantsList;
    private ArrayList<Restaurant> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize storage handler
        storage = new RestaurantStorage(this);

        // Initialize views
        listview = findViewById(R.id.restaurantList);
        searchBar = findViewById(R.id.searchBar);

        restaurantsList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Set up adapter
        adapter = new RestaurantAdapter(this, filteredList);
        listview.setAdapter(adapter);

        // Set up search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Load all restaurants initially
        loadRestaurants();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload restaurants when returning to this activity
        searchBar.setText("");
        loadRestaurants();
    }

    // Method to load all restaurants from database
    private void loadRestaurants() {
        restaurantsList.clear();
        filteredList.clear();

        // Get all restaurants from storage
        List<Restaurant> dbRestaurants = storage.getAllRestaurants();
        restaurantsList.addAll(dbRestaurants);
        filteredList.addAll(dbRestaurants);

        // Notify adapter of data change
        adapter.notifyDataSetChanged();
    }

    // Method to filter restaurants based on search query
    private void filterRestaurants(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            // If search is empty, show all restaurants
            filteredList.addAll(restaurantsList);
        } else {
            // Filter restaurants by name
            List<Restaurant> searchResults = storage.searchRestaurants(query);
            filteredList.addAll(searchResults);
        }

        adapter.notifyDataSetChanged();
    }

    // Method called when "Log a Restaurant" button is clicked
    public void addRestaurant(View view) {
        Intent intent = new Intent(this, AddEditRestaurantActivity.class);
        startActivity(intent);
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