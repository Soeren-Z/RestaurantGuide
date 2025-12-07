package com.example.restaurantguide.ui.activities;

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

import com.example.restaurantguide.R;
import com.example.restaurantguide.adapters.RestaurantAdapter;
import com.example.restaurantguide.db.AppDatabase;
import com.example.restaurantguide.models.RestaurantWithTags;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    private RestaurantAdapter adapter;
    private ListView listview;
    private ArrayList<RestaurantWithTags> restaurantsList;
    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = AppDatabase.getInstance(this);
        listview = findViewById(R.id.restaurantList);
        searchBar = findViewById(R.id.searchBar);
        setupSearchBar();
        loadRestaurants();
    }
    
    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadRestaurants();
                } else {
                    searchRestaurants(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    
    private void searchRestaurants(String query) {
        db.restaurantDao().searchRestaurantsWithTags(query).observe(this, restaurantsList -> {
            if(adapter == null) {
                adapter = new RestaurantAdapter(this, new ArrayList<>(restaurantsList));
                listview.setAdapter(adapter);
            } else {
                adapter.updateList(restaurantsList);
            }
        });
    }
    private void loadRestaurants() {
        db.restaurantDao().getAllRestaurantWithTags().observe(this, restaurantsList -> {
            if(adapter == null) {
                adapter = new RestaurantAdapter(this, new ArrayList<>(restaurantsList));
                listview.setAdapter(adapter);
            } else {
                adapter.updateList(restaurantsList);
            }
        });
    }
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
    protected void onResume() {
        super.onResume();
        loadRestaurants();
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