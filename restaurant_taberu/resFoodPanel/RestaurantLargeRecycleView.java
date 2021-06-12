package com.example.restaurant_taberu.resFoodPanel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;


import com.example.restaurant_taberu.Delete_Dish;
import com.example.restaurant_taberu.R;

import java.util.ArrayList;

public class RestaurantLargeRecycleView extends RecyclerView.Adapter<RestaurantLargeRecycleView.ViewHolder> {

    private ArrayList<FoodDetails> platillo;
    private int lastPosition = -1;
    private Context context;

    public RestaurantLargeRecycleView(ArrayList<FoodDetails> platillo) {
        this.platillo = platillo;
    }

    @NonNull
    @Override
    public RestaurantLargeRecycleView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card_large_recycle, null, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantLargeRecycleView.ViewHolder holder, int position) {
        ((TextView) holder.getCategoryCard().findViewById(R.id.restaurantName)).setText(
                platillo.get(position).getNombre());

        ((TextView) holder.getCategoryCard().findViewById(R.id.cardRestaurantDesc2)).setText(
                platillo.get(position).getDescripcion());


        if (platillo.get(position).getBitmap() != null) {
            ((ImageView) holder.getCategoryCard().findViewById(R.id.cardRestaurantImg2)).setImageBitmap(
                    platillo.get(position).getBitmap()
            );
        }

        setAnimation(holder.itemView, position);

        holder.getCategoryCard().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Delete_Dish.class);
                intent.putExtra("PLATILLO_ID", platillo.get(position).getId());
                intent.putExtra("Nombre",platillo.get(position).getNombre());
                intent.putExtra("Categoria",platillo.get(position).getCategoria());
                intent.putExtra("Ingredientes",platillo.get(position).getIngredientes());
                intent.putExtra("Descripcion",platillo.get(position).getDescripcion());
                intent.putExtra("Precio",platillo.get(position).getPrecio());
                context.startActivity(intent);
            }


            //holder.getCategoryCard().setOnClickListener((view) -> {
            //Intent intent = new Intent(context, RestaurantHomeActivity.class);

//            TextView name = view.findViewById(R.id.restaurantName);
//            TextView time = view.findViewById(R.id.cardRestaurantTime2);
//            TextView raiting = view.findViewById(R.id.cardRestaurantRate2);
//
//            Pair<View, String> p1 = Pair.create((View)name, "restaurantName");
//            Pair<View, String> p2 = Pair.create((View)time, "waitingTime");
//            Pair<View, String> p3 = Pair.create((View)raiting, "raiting");
//
//            intent.putExtra("RESTAURANT_NAME", restaurant.get(position).getNombre());
//            intent.putExtra("RESTAURANT_TIME", "30 - 40 min");
//            intent.putExtra("RESTAURANT_RATE", restaurant.get(position).getPuntuacion() + " Estrellas");
//            intent.putExtra("RESTAURANT_CATEGORY", restaurant.get(position).getCategoria());
//
//            intent.putExtra("RESTAURANT_ID", restaurant.get(position).getId());
//
//            ActivityOptionsCompat option = ActivityOptionsCompat.
//                    makeSceneTransitionAnimation((Activity) context, p1, p2, p3);
//
//            context.startActivity(intent, option.toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return platillo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout categoryCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCard = (ConstraintLayout) itemView.findViewById(R.id.cardRestaurant2);
        }

        public ConstraintLayout getCategoryCard() {
            return this.categoryCard;
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition && position <= 0)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setStartOffset(250 + 200 / (position+1));
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        } else {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setStartOffset(250);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
