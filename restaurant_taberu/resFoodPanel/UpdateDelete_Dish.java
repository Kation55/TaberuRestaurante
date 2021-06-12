package com.example.restaurant_taberu.resFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import com.example.restaurant_taberu.R;
import com.example.restaurant_taberu.UpdateDishModel;
import com.example.restaurant_taberu.ResFoodPanel_BottomNavi;

public class UpdateDelete_Dish extends AppCompatActivity {

    TextInputLayout desc,ing,pri;
    TextView Dishname;
    ImageButton imageButton;
    Uri imageuri;
    String dburi;
    private Uri mCropimageuri;
    Button Update_dish,Delete_dish;
    String description,ingredientes,price,dishes,ChefId;
    String RandomUID;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth FAuth;
    FirebaseFirestore databaseReference;
    String ID, userid;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete__dish);

        databaseReference = FirebaseFirestore.getInstance();

        desc = (TextInputLayout)findViewById(R.id.description);
        ing = (TextInputLayout) findViewById(R.id.Ingredientes);
        pri = (TextInputLayout)findViewById(R.id.price);
        Dishname = (TextView)findViewById(R.id.dish_name);
        imageButton = (ImageButton) findViewById(R.id.image_upload);
        Update_dish = (Button)findViewById(R.id.Updatedish);
        Delete_dish = (Button)findViewById(R.id.Deletedish);
        ID = getIntent().getStringExtra("updatedeletedish");

        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Update_dish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = desc.getEditText().getText().toString().trim();
                ingredientes = ing.getEditText().getText().toString().trim();
                price = pri.getEditText().getText().toString().trim();

                if(isValid()){
                    if(imageuri != null){
                        uploadImage();
                    }else{
                        updatedesc(dburi);
                    }
                }

            }
        });

//        Delete_dish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDelete_Dish.this);
//                builder.setMessage("¿ Estas seguro de borrar el platillo ?");
//                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        firebaseDatabase.getInstance().getReference("FoodDetails").child(Estado).child(Ciudad).child(Calle)
//                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();
//                        AlertDialog.Builder food = new AlertDialog.Builder(UpdateDelete_Dish.this);
//                        food.setMessage("Tu platillo ha sido eliminado");
//                        food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                startActivity(new Intent(UpdateDelete_Dish.this, ResFoodPanel_BottomNavi.class));
//                            }
//                        });
//                        AlertDialog alert = food.create();
//                        alert.show();
//                    }
//                });
//                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });

//        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Restaurante cheff = snapshot.getValue(Restaurante.class);
//
//                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                progressDialog = new ProgressDialog(UpdateDelete_Dish.this);
//                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(Estado).child(Ciudad).child(Calle)
//                        .child(useridd).child(ID);
//                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UpdateDishModel updateDishModel = snapshot.getValue(UpdateDishModel.class);
//                        desc.getEditText().setText(updateDishModel.getDescripcion());
//                        ing.getEditText().setText(updateDishModel.getIngredientes());
//                        Dishname.setText("Dish name:"+updateDishModel.getDishes());
//                        dishes=updateDishModel.getDishes();
//                        pri.getEditText().setText(updateDishModel.getPrice());
//                        Glide.with(UpdateDelete_Dish.this).load(updateDishModel.getImageURL()).into(imageButton);
//                        dburi = updateDishModel.getImageURL();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                FAuth = FirebaseAuth.getInstance();
//                databaseReference = firebaseDatabase.getInstance().getReference("FoodDetails");
//                storage = FirebaseStorage.getInstance();
//                storageReference = storage.getReference();
//                imageButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onSelectImageclick(v);
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void updatedesc(String buri) {

//        ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        FoodDetails info = new FoodDetails(dishes,ingredientes,price,description,buri,ID,ChefId);
//        firebaseDatabase.getInstance().getReference("FoodDetails").child(Estado).child(Ciudad).child(Calle)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
//                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                progressDialog.dismiss();
//                Toast.makeText(UpdateDelete_Dish.this,"Platillo actualizado correctamente!",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void uploadImage() {

        if(imageuri != null){

            progressDialog.setTitle("Subiendo");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updatedesc(String.valueOf(uri));
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateDelete_Dish.this,"Fallo:"+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Subiendo "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }

    private boolean isValid() {

        desc.setErrorEnabled(false);
        desc.setError("");
        ing.setErrorEnabled(false);
        ing.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValidDescription = false,isValidPrice=false,isValidQuantity=false,isValid=false;
        if(TextUtils.isEmpty(description)){
            desc.setErrorEnabled(true);
            desc.setError("Se necesita una descripcion");
        }else{
            desc.setError(null);
            isValidDescription=true;
        }
        if(TextUtils.isEmpty(ingredientes)){
            ing.setErrorEnabled(true);
            ing.setError("Ingresa la cantidad en gramos");
        }else{
            isValidQuantity=true;
        }
        if(TextUtils.isEmpty(price)){
            pri.setErrorEnabled(true);
            pri.setError("Por favor ingresa precio");
        }else{
            isValidPrice=true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice)?true:false;
        return isValid;
    }

    private void startCropImageActivity(Uri imageuri){
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
    private void onSelectImageclick(View v){
        CropImage.startPickImageActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(mCropimageuri !=null && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startCropImageActivity(mCropimageuri);
        }else{
            Toast.makeText(this,"No tienes permisos",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            imageuri = CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                mCropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCropImageActivity(imageuri);
            }
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                ((ImageButton) findViewById(R.id.image_upload)).setImageURI(result.getUri());
                Toast.makeText(this,"Recorte realizado correctamente"+result.getSampleSize(),Toast.LENGTH_SHORT).show();
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this,"Fallo recorte"+result.getError(),Toast.LENGTH_SHORT).show();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}