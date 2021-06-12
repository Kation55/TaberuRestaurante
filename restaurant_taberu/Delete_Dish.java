package com.example.restaurant_taberu;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant_taberu.resFoodPanel.FoodDetails;
import com.example.restaurant_taberu.resFoodPanel.RestaurantService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class Delete_Dish extends AppCompatActivity {

    Button delete;
    FirebaseAuth Fauth;
    FirebaseFirestore databaseReference;
    FoodDetails fdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_dish_activity);


        EditText dish = findViewById(R.id.dishes);
        EditText ing = findViewById(R.id.Ingredientes);
        EditText desc = findViewById(R.id.description);
        EditText cat = findViewById(R.id.ResCategory);
        EditText price = findViewById(R.id.price);

        databaseReference = FirebaseFirestore.getInstance();
        Fauth = FirebaseAuth.getInstance();



        dish.setText(getIntent().getExtras().getString("Nombre"));
        ing.setText(getIntent().getExtras().getString("Ingredientes"));
        desc.setText(getIntent().getExtras().getString("Descripcion"));
        cat.setText(getIntent().getExtras().getString("Categoria"));
        //price.setText(getIntent().getExtras().getInt("Precio"));

        delete = (Button)findViewById(R.id.post);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDish();

            }
        });



    }

    private void deleteDish() {

        String userId = Fauth.getInstance().getCurrentUser().getUid();
        String dirId = getIntent().getExtras().getString("PLATILLO_ID");

        //AddressDetails addetails = new AddressDetails(ali, next, desc, country, state, cal01, cal02, ref, pincode);
        databaseReference.collection("Restaurante").document(userId).collection("Platillos").document(dirId)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Delete_Dish.this, "Se ha borrado el platillo!", Toast.LENGTH_SHORT).show();
               // Intent b = new Intent(Delete_Dish.this, HomeActivity.class);
                //tartActivity(b);
            }
        });
    }




}