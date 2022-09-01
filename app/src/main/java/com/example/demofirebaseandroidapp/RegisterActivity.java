package com.example.demofirebaseandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password;
    Button register;

    private FirebaseAuth auth;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = findViewById(R.id.fieldEmail);
        password = findViewById(R.id.fieldPassword);
        register = findViewById(R.id.btnR);

        auth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    progress.show();
                    String txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    registerUser(txt_email, txt_password);
            }
        });




    }

    private void registerUser(String txt_email, String txt_password) {
            auth.createUserWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>(){

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       progress.hide();
                       Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                   }
                   else {
                       progress.hide();
                       Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
                }
            });


    }
}