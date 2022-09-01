package com.example.demofirebaseandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button register,login;
    private ProgressDialog progress;
    private FirebaseAuth auth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        register = findViewById(R.id.btnRegister);
        login = findViewById(R.id.btnLogin);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();

                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                progress.hide();


            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                progress.hide();

            }
        });




    }
    @Override
    protected  void onStart() {
        if(user != null){
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();

        }

        super.onStart();
    }


}