package com.example.restaurantguide.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurantguide.R;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private String restaurantName;
    private String restaurantAddress;
    private TextView nameTextView;
    private TextView addressTextView;
    private Button getDirectionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize osmdroid configuration
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE));
        Configuration.getInstance().setUserAgentValue(getPackageName());
        
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get restaurant data from intent
        Intent intent = getIntent();
        restaurantName = intent.getStringExtra("restaurant_name");
        restaurantAddress = intent.getStringExtra("restaurant_address");

        // Set up UI elements
        nameTextView = findViewById(R.id.restaurantNameTextView);
        addressTextView = findViewById(R.id.restaurantAddressTextView);
        getDirectionsButton = findViewById(R.id.getDirectionsButton);
        mapView = findViewById(R.id.map);

        if (restaurantName != null) {
            nameTextView.setText(restaurantName);
        }
        if (restaurantAddress != null) {
            addressTextView.setText(restaurantAddress);
        }

        // Set up directions button - opens Google Maps
        getDirectionsButton.setOnClickListener(v -> {
            if (restaurantAddress != null && !restaurantAddress.isEmpty()) {
                android.net.Uri gmmIntentUri = android.net.Uri.parse("geo:0,0?q=" + android.net.Uri.encode(restaurantAddress));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                try {
                    startActivity(mapIntent);
                } catch (android.content.ActivityNotFoundException e) {
                    // Fallback to web browser if no app can handle geo: URI
                    android.net.Uri webUri = android.net.Uri.parse("https://www.google.com/maps/search/?api=1&query=" + android.net.Uri.encode(restaurantAddress));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webUri);
                    try {
                        startActivity(webIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(this, "Unable to open maps", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Set up map
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);
        
        // Load restaurant location
        loadRestaurantLocation();
    }

    private void loadRestaurantLocation() {
        if (restaurantAddress != null && !restaurantAddress.isEmpty()) {
            // Geocode the address to get coordinates
            new Thread(() -> {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                try {
                    List<android.location.Address> addresses = geocoder.getFromLocationName(restaurantAddress, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        android.location.Address address = addresses.get(0);
                        double latitude = address.getLatitude();
                        double longitude = address.getLongitude();
                        
                        runOnUiThread(() -> {
                            // Create GeoPoint for osmdroid
                            GeoPoint location = new GeoPoint(latitude, longitude);
                            
                            // Add marker for restaurant
                            Marker marker = new Marker(mapView);
                            marker.setPosition(location);
                            String markerTitle = restaurantName != null ? restaurantName : "Restaurant";
                            marker.setTitle(markerTitle);
                            marker.setSnippet(restaurantAddress);
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            mapView.getOverlays().add(marker);
                            
                            // Move camera to restaurant location
                            mapView.getController().setZoom(15.0);
                            mapView.getController().setCenter(location);
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Could not find location for address", Toast.LENGTH_SHORT).show();
                            // Default to Toronto if geocoding fails
                            GeoPoint defaultLocation = new GeoPoint(43.6532, -79.3832);
                            mapView.getController().setZoom(10.0);
                            mapView.getController().setCenter(defaultLocation);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error geocoding address", Toast.LENGTH_SHORT).show();
                        // Default to Toronto if geocoding fails
                        GeoPoint defaultLocation = new GeoPoint(43.6532, -79.3832);
                        mapView.getController().setZoom(10.0);
                        mapView.getController().setCenter(defaultLocation);
                    });
                }
            }).start();
        } else {
            // Default to Toronto if no address
            GeoPoint defaultLocation = new GeoPoint(43.6532, -79.3832);
            mapView.getController().setZoom(10.0);
            mapView.getController().setCenter(defaultLocation);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
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