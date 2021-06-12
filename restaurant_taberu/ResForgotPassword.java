package com.example.restaurant_taberu;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResForgotPassword extends AppCompatActivity
{

    private Button resetB;
    private EditText ResetEmail;
    private FirebaseAuth FAuth;
    private EditText remail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        FAuth = FirebaseAuth.getInstance();

        resetB = (Button)findViewById(R.id.button4);
        remail = (EditText)findViewById(R.id.inputLoginEmail);

        resetB.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String uemail = remail.getText().toString();
                if(TextUtils.isEmpty(uemail))
                {
                    Toast.makeText(ResForgotPassword.this,"Valida que tu correo electronico sea correcto",Toast.LENGTH_LONG);
                }
                else
                {
                    FAuth.sendPasswordResetEmail(uemail).addOnCompleteListener(new OnCompleteListener<Void>(){

                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResForgotPassword.this,"Revisa tu correo para reestablecer tu contraseña",Toast.LENGTH_LONG);
                                startActivity(new Intent(ResForgotPassword.this, Reslogin.class));
                                finish();
                            }
                            else
                            {

                                Toast.makeText(ResForgotPassword.this,"Ha ocurrido un error",Toast.LENGTH_LONG);
                            }
                        }
                    });
                }
            }
        });

    }



}
