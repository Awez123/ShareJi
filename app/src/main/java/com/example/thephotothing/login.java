package com.example.thephotothing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    private EditText email,password;
    private Button login,register;
    public static String email_profile;
    FirebaseAuth mauth;
    private String txt_email,txt_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email_login);
        password= findViewById(R.id.password_login);
        login= findViewById(R.id.login_login);
        register= findViewById(R.id.register_login);
        mauth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_email = email.getText().toString();
                txt_pass = password.getText().toString();

                mauth.signInWithEmailAndPassword(txt_email,txt_pass).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         email_profile = txt_email;
                         Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(login.this, ImageLoader.class);
                         intent.putExtra("email",txt_email);
                         startActivity(intent);
                     }else {
                         Toast.makeText(login.this, "User Doesnt Exist", Toast.LENGTH_SHORT).show();
                     }
                    }
                });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,Register.class));
                finish();
            }
        });



    }
}