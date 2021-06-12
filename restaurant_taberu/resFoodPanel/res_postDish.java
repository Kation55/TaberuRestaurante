package com.example.restaurant_taberu.resFoodPanel;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import com.example.restaurant_taberu.R;

public class res_postDish extends AppCompatActivity {

    ImageButton imageButton;
    Button post_dish;;
    EditText desc,ing,pri, category,Dishes;
    String descrption,ingredientes,price,dish;
    Uri imageuri;
    private Uri mcropimageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseFirestore databaseReference;
    FirebaseAuth Fauth;
    StorageReference ref;
    String ChefId , RandomUID, userid, cat;
    CheckBox isportada;
    int nprice=0;
    Boolean bportada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_post_dish);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Dishes = (EditText)findViewById(R.id.dishes);
        desc = (EditText)findViewById(R.id.description);
        ing = (EditText)findViewById(R.id.Ingredientes);
        pri = (EditText)findViewById(R.id.price);
        post_dish = (Button) findViewById(R.id.post);
        category = (EditText) findViewById(R.id.ResCategory);

        isportada=(CheckBox)findViewById(R.id.checkBoxPortada);
        Fauth = FirebaseAuth.getInstance();

        databaseReference = FirebaseFirestore.getInstance();

        try {
            userid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            imageButton = (ImageButton) findViewById(R.id.image_upload);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectImageclick(v);
                }
            });

            post_dish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dish = Dishes.getText().toString().trim();
                    descrption = desc.getText().toString().trim();
                    ingredientes = ing.getText().toString().trim();
                    price = pri.getText().toString().trim();
                    cat = category.getText().toString().trim();

                    try {
                        nprice = Integer.parseInt(pri.getText().toString());

                    }
                    catch(NumberFormatException nfe) {

                        System.out.println("Could not parse " + nfe);
                    }

                    if(isportada.isChecked())
                    {
                        bportada = true;
                    }
                    else
                    {
                        bportada = false;
                    }

                    if(isValid()){
                        uploadImage();
                    }
                }
            });
        }catch (Exception e){
            Log.e("Error: ",e.getMessage());
        }

    }

    private void uploadImage() {

        if(imageuri != null){
            final ProgressDialog progressDialog = new ProgressDialog(res_postDish.this);
            progressDialog.setTitle("Subiendo.....");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            Log.e("UserID: ",userid);
            ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {


                            FoodDetails foodDetails = new FoodDetails(dish,descrption,nprice,ingredientes,cat,bportada,String.valueOf(uri));
                            databaseReference.collection("Restaurante").document(userid).collection("Platillos").document()
                                    .set(foodDetails.getMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(res_postDish.this,"Tu platillo ha sido publicado!",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(res_postDish.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Subiendo  "+(int) progress+"%");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
        }

    }

    private boolean isValid() {


        desc.setError("");

        ing.setError("");

        pri.setError("");

        boolean isValidDescription = false,isValidPrice=false,isValidQuantity=false,isValid=false, isValidCat = false;
        if(TextUtils.isEmpty(descrption)){

            desc.setError("Se requiere descripción");
        }else{
            desc.setError(null);
            isValidDescription=true;
        }
        if(TextUtils.isEmpty(ingredientes)){

            ing.setError("Por favor ingresa ingredientes");
        }else{
            isValidQuantity=true;
        }
        if(TextUtils.isEmpty(price)){

            pri.setError("Se requiere precio");
        }else{
            isValidPrice=true;
        }
        if(TextUtils.isEmpty(cat)){

            pri.setError("Se requiere Categoria");
        }else{
            isValidCat=true;
        }
        isValid = (isValidDescription && isValidQuantity && isValidPrice && isValidCat)?true:false;
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
        if(mcropimageuri !=null && grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            startCropImageActivity(mcropimageuri);
        }else{
            Toast.makeText(this,"No tienes los permisos necesarios ",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            imageuri = CropImage.getPickImageResultUri(this,data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                mcropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCropImageActivity(imageuri);
            }
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                ((ImageButton) findViewById(R.id.image_upload)).setImageURI(result.getUri());
                Toast.makeText(this,"Imagen recortada",Toast.LENGTH_SHORT).show();
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this,"Fallo recorte"+result.getError(),Toast.LENGTH_SHORT).show();

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}