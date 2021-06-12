package com.example.restaurant_taberu.resFoodPanel;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class RestaurantService {

    private static RestaurantService restaurantService;
    private ArrayList<FoodDetails> platillosArrayList;
    private FirebaseFirestore db;

    public RestaurantService() {
        db = FirebaseFirestore.getInstance();
        platillosArrayList = new ArrayList<>();
    }

    public static RestaurantService getInstance() {
        if (restaurantService == null)
            restaurantService = new RestaurantService();

        return restaurantService;
    }

    public Future<ArrayList> getRestaurants () {
        CompletableFuture<ArrayList> status = new CompletableFuture<>();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        platillosArrayList.clear();

        db.collection("Restaurante").document(userId).collection("Platillos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getDocuments().forEach( documentSnapshot -> {
                        FoodDetails platillos = FoodDetails.of(documentSnapshot.getData());

                        try {
                            platillos.setId(documentSnapshot.getId());
                            platillosArrayList.add(platillos);
                        } catch (Exception ignored) {
                        }
                    });

                    status.complete(platillosArrayList);
                }
            }
        });

        return status;
    }


    public Future<String> loadImage(String id) {
        CompletableFuture<String> status = new CompletableFuture<>();

        db.collection("Restaurante").document(id).collection("Platillos")
                .get().addOnCompleteListener(
            new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()) {

                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            status.complete((String)documentSnapshot.get("ImageURL"));
                        } else {
                            status.complete(null);
                        }
                    } else {

                        status.complete(null);
                    }
                }
            }
        );

        return status;
    }
}


