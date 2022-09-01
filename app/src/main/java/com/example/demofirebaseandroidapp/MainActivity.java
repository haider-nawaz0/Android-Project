package com.example.demofirebaseandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivity extends AppCompatActivity {

    Button register,login;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                progress.hide();
                finish();
            }
        });


    }
}