package com.example.demofirebaseandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnLetsGo;

    private FirebaseAuth auth;
    public static FirebaseUser user;
    private View parentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        btnLetsGo = findViewById(R.id.btnLetsGo);

        parentLayout = findViewById(android.R.id.content);

        btnLetsGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });




    }
    @Override
    protected  void onStart() {
        if(user != null){

            Snackbar.make(parentLayout, "Logged in as"+user.getEmail().toString(), Snackbar.LENGTH_SHORT)
                    .show();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();

        }else {
            Snackbar.make(parentLayout, "No user logged in!", Snackbar.LENGTH_SHORT)
                    .show();
        }

        super.onStart();
    }


}