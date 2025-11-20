package com.example.restaurantguide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
    private Context context;
    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        super(context, 0, restaurants);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = (Restaurant) getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_row, parent, false);
        }
        TextView name = convertView.findViewById(R.id.restaurantName);
        RatingBar rating = convertView.findViewById(R.id.restaurantRating);
        TextView address = convertView.findViewById(R.id.restaurantAddress);
        TextView tags = convertView.findViewById(R.id.restaurantTags);
        name.setText(restaurant.getName());
        rating.setRating(restaurant.getRating());
        address.setText(restaurant.getAddress());
        tags.setText(restaurant.getTags().toString());

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra("restaurant_id", restaurant.getId());
            context.startActivity(intent);
        });
        /*convertView.setOnClickListener(v -> {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        });*/
        return convertView;
    }
}
