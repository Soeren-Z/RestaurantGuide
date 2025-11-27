package com.example.restaurantguide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartUpActivity extends AppCompatActivity {

    // Set variables for logging new restaurant and about team text
    private Button logRestaurantButton;
    private TextView aboutTeamText;

    // onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        logRestaurantButton = findViewById(R.id.logRestaurantButton);
        aboutTeamText = findViewById(R.id.aboutTeamText);

        // Click listener for Log Restaurant button -> sends you to MainActivity
        logRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Click listener for About Team text -> sends you to TeamInfoActivity
        aboutTeamText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartUpActivity.this, TeamInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}