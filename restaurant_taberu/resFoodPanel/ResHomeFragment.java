package com.example.restaurant_taberu.resFoodPanel;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurant_taberu.R;


import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ResHomeFragment extends Fragment {
    private ArrayList<String> categoryName;
    private ArrayList<Drawable> categoryIcon;

    private RecyclerView plat;
    private RecyclerView categoryCards;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.home_search,null);


        //EditText editText = (EditText) v.findViewById(R.id.inputAddressFormAddress3);

        plat = (RecyclerView) v.findViewById(R.id.searchRestaurantRecycle);
        plat.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        LoadCardRecycle loadCardRecycle = new LoadCardRecycle();

        plat.setAdapter(loadCardRecycle);

      //CategoryRecycleView categoryRecycleView = new CategoryRecycleView(categoryName, categoryIcon, editText, restaurants);
       //categoryCards.setAdapter(categoryRecycleView);

        new LoadRestaurants(v, null).execute();

        return v;
    }


    private void setRestaurants(RecyclerView recyclerView, ArrayList<FoodDetails> platillos) {
        RestaurantLargeRecycleView restaurantRecycleView = new RestaurantLargeRecycleView(platillos);
        recyclerView.setAdapter(restaurantRecycleView);
    }

    public class LoadRestaurants extends AsyncTask<Void, Void, Boolean> {

        private View view;
        private String filter;
        private ArrayList<FoodDetails> platillos;

        public LoadRestaurants(View view, String filter) {
            this.view = view;
            this.filter = filter;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                platillos = RestaurantService.getInstance().getRestaurants().get();

                platillos.forEach( platillo -> {
                    try {
                        String img = RestaurantService.getInstance().loadImage(platillo.getId()).get();
                        if (img != null) {
                            URL newurl = new URL(img);
                            Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
                            platillo.setBitmap(mIcon_val);
                        } else {
                            platillo.setBitmap(null);
                        }
                    } catch (ExecutionException | InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                setRestaurants(plat, platillos);
            }
        }
    }
}