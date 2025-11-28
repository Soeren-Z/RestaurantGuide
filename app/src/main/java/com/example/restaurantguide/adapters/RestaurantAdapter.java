package com.example.restaurantguide.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.restaurantguide.R;
import com.example.restaurantguide.ui.activities.RestaurantDetailsActivity;
import com.example.restaurantguide.models.RestaurantWithTags;
import com.example.restaurantguide.models.Tag;

import java.util.List;

public class RestaurantAdapter extends ArrayAdapter<RestaurantWithTags> {
    private Context context;
    public RestaurantAdapter(Context context, List<RestaurantWithTags> restaurants) {
        super(context, 0, restaurants);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RestaurantWithTags restaurant = (RestaurantWithTags) getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.restaurant_row, parent, false);
        }
        TextView name = convertView.findViewById(R.id.restaurantName);
        RatingBar rating = convertView.findViewById(R.id.restaurantRating);
        TextView address = convertView.findViewById(R.id.restaurantAddress);
        TextView tags = convertView.findViewById(R.id.restaurantTags);
        name.setText(restaurant.getRestaurant().getName());
        rating.setRating(restaurant.getRestaurant().getRating());
        address.setText(restaurant.getRestaurant().getAddress());

        List<Tag> tagsList = restaurant.getTags();
        if(tagsList != null && !tagsList.isEmpty()) {
            StringBuilder tagString = new StringBuilder();
            for (int i = 0; i < tagsList.size(); i++) {
                tagString.append(tagsList.get(i).getTagName());
                if (i != tagsList.size() - 1) tagString.append(", ");
            }
            tags.setText(tagString.toString());
        } else {
            tags.setText("");
        }

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(context, RestaurantDetailsActivity.class);
            intent.putExtra("restaurant_id", restaurant.getRestaurant().getRestaurantId());
            context.startActivity(intent);
        });
        return convertView;
    }
}
