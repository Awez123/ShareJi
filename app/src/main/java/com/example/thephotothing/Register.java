package com.example.thephotothing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Register extends AppCompatActivity {


    private EditText name,email,password,repassword;
    private Button register,login;
    public static String email_profile;
    public String txt_name,txt_email,txt_password,txt_repassword;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name_register);
        email=findViewById(R.id.email_register);
        password=findViewById(R.id.password_register);
        repassword=findViewById(R.id.re_password_register);
        register=findViewById(R.id.register_register);
        login=findViewById(R.id.login_register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,login.class));
            }
        });

        mauth = FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_name=name.getText().toString();
                txt_password=password.getText().toString();
                txt_email=email.getText().toString();
                txt_repassword=repassword.getText().toString();
                if (txt_name.isEmpty()||txt_email.isEmpty()||txt_password.isEmpty()||txt_repassword.isEmpty()){
                    Toast.makeText(Register.this, "Empty views", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(txt_password.length()>10){
                        Toast.makeText(Register.this, "pass len", Toast.LENGTH_SHORT).show();

                    }

                    else if (txt_password.equals(txt_repassword)) {
                        mauth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(Register.this, task -> {
                            if (task.isSuccessful()){
                                email_profile = txt_email;
                                Intent intent = new Intent(Register.this,ImageLoader.class);
                                intent.putExtra("email",txt_email);

                                startActivity(intent);

                                finish();

                            }

                        });

                    }
                }

            }
        });


    }

}