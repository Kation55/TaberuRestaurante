package com.example.restaurant_taberu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restaurant_taberu.resFoodPanel.Restaurante;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Reslogin extends AppCompatActivity {

    EditText email,pass;
    Button Signin;
    TextView Forgotpassword , signup;
    FirebaseAuth Fauth;
    String emailid,pwd;
    FirebaseFirestore databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reslogin);

        try{

            email = (EditText)findViewById(R.id.Lemail);
            pass = (EditText)findViewById(R.id.Lpassword);
            Signin = (Button)findViewById(R.id.buttonLogin);
            signup = (TextView) findViewById(R.id.textButtonRegister);
            Forgotpassword = (TextView)findViewById(R.id.textButtonForgotPassword);


            databaseReference = FirebaseFirestore.getInstance();
            Fauth = FirebaseAuth.getInstance();

            Signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    emailid = email.getText().toString().trim();
                    pwd = pass.getText().toString().trim();

                    if(isValid()){

                        final ProgressDialog mDialog = new ProgressDialog(Reslogin.this);
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("Ingresando a tu cuenta");
                        mDialog.show();

                        Fauth.signInWithEmailAndPassword(emailid,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    String useridd = Fauth.getCurrentUser().getUid();
                                    DocumentReference docRef = databaseReference.collection("Restaurante").document(useridd);

                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists())
                                                {

                                                    Restaurante.setRestaurante(Restaurante.of(document.getData()));


                                                }

                                            }
                                        }
                                    });

                                    mDialog.dismiss();

                                    if(Fauth.getCurrentUser().isEmailVerified()){
                                        mDialog.dismiss();
                                        Toast.makeText(Reslogin.this, "Se ha ingresado a la cuenta correctamente", Toast.LENGTH_SHORT).show();
                                        Intent Z = new Intent(Reslogin.this,ResFoodPanel_BottomNavi.class);
                                        startActivity(Z);
                                        finish();

                                    }else{
                                        ReusableCode.ShowAlert(Reslogin.this,"Verificación Fallida","Por favor vetifica tu correo electronico");

                                    }
                                }else{
                                    mDialog.dismiss();
                                    ReusableCode.ShowAlert(Reslogin.this,"Error",task.getException().getMessage());
                                }
                            }
                        });
                    }
                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Reslogin.this,ResRegistration.class));
                    finish();
                }
            });
            Forgotpassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Reslogin.this,ResForgotPassword.class));
                    finish();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }
    String emailpattern  = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public boolean isValid(){

        email.setError("");
        pass.setError("");

        boolean isvalid=false,isvalidemail=false,isvalidpassword=false;
        if(TextUtils.isEmpty(emailid)){
            email.setError("Email requerido");
        }else{
            if(emailid.matches(emailpattern)){
                isvalidemail=true;
            }else{
                email.setError("Dirección Email No Valida");
            }
        }
        if(TextUtils.isEmpty(pwd)){

            pass.setError("Se Requiere Password");
        }else{
            isvalidpassword=true;
        }
        isvalid=(isvalidemail && isvalidpassword)?true:false;
        return isvalid;
    }
}