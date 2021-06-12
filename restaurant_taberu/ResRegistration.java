package com.example.restaurant_taberu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.restaurant_taberu.resFoodPanel.Restaurante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ResRegistration extends AppCompatActivity {


    EditText Fname,Ctegoria,Email,Pass,cpass,mobileno,Descrip;
    Button signup, emaill, phone;
    FirebaseAuth FAuth;
    FirebaseFirestore databaseReference;
    String fname,ctegoria,emailid,password,confpassword,mobile,desc;
    String role="Restaurante";
    int npincode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_registration);

        databaseReference = FirebaseFirestore.getInstance();

        Fname = findViewById(R.id.inputRegisterName);
        Ctegoria = findViewById(R.id.inputRegisterCat);
        Descrip = findViewById(R.id.inputRegisterDesc);
        Email = findViewById(R.id.inputRegisterEmail);
        Pass = findViewById(R.id.inputRegisterPassword);
        cpass = findViewById(R.id.inputRegisterPassword2);
        mobileno = findViewById(R.id.inputRegisterPhone);


        signup = (Button)findViewById(R.id.button4);






        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fname = Fname.getText().toString().trim();
                ctegoria = Ctegoria.getText().toString().trim();
                emailid = Email.getText().toString().trim();
                mobile = mobileno.getText().toString().trim();
                password = Pass.getText().toString().trim();
                confpassword = cpass.getText().toString().trim();
                desc = Descrip.getText().toString().trim();



                if (isValid()){
                    final ProgressDialog mDialog = new ProgressDialog(ResRegistration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registro en proceso, por favor espera");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                Restaurante restaurante = new Restaurante(fname,desc, emailid, mobile, ctegoria);


                                databaseReference.collection("Restaurante").document(useridd).set(restaurante.getMap())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {


                                            @Override
                                            public void onSuccess(Void unused) {
                                                mDialog.dismiss();

                                                if(task.isSuccessful()){
                                                    FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(ResRegistration.this);
                                                                builder.setMessage("Registro completo, verifica tu correo");
                                                                builder.setCancelable(false);
                                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        dialog.dismiss();
                                                                        startActivity(new Intent(ResRegistration.this, Reslogin.class));
                                                                        finish();

                                                                    }
                                                                });
                                                                AlertDialog Alert = builder.create();
                                                                Alert.show();
                                                            } else {
                                                                mDialog.dismiss();
                                                                ReusableCode.ShowAlert(ResRegistration.this, "Error", task.getException().getMessage());
                                                            }
                                                        }
                                                    });

                                                }else{
                                                    mDialog.dismiss();
                                                    ReusableCode.ShowAlert(ResRegistration.this,"Error",task.getException().getMessage());
                                                }
                                            }
                                        });
                            }
                        }
                    });
                }
//
            }
        });



    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        Email.setError("");
        Fname.setError("");
        Ctegoria.setError("");
        Pass.setError("");
        mobileno.setError("");
        cpass.setError("");


        boolean isValid=false,isValidlname=false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenum=false;
        if(TextUtils.isEmpty(fname)){
            Fname.setError("Ingresa Nombre");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(ctegoria)){
            Ctegoria.setError("Ingresa Categoría");
        }else{
            isValidlname = true;
        }
        if(TextUtils.isEmpty(emailid)){
            Email.setError("Se requiere correo");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                Email.setError("Ingresa un correo válido");
            }
        }
        if(TextUtils.isEmpty(password)){
            Pass.setError("Ingresa Password");
        }else{
            if(password.length()<8){
                Pass.setError("Tu Password es muy sencillo");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            cpass.setError("Ingresa Password nuevamente");
        }else{
            if(!password.equals(confpassword)){
                cpass.setError("Password diferentes");
            }else{
                isValidconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            mobileno.setError("Tu teléfono es requerido");
        }else{
            if(mobile.length()<10){
                mobileno.setError("Teléfono invalido");
            }else{
                isValidmobilenum = true;
            }
        }

        isValid = (isValidconfpassword && isValidpassword && isValidemail && isValidmobilenum && isValidname && isValidlname) ? true : false;
        return isValid;


    }
}