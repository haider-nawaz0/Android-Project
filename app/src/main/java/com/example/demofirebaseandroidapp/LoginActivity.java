package com.example.demofirebaseandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button login;
    private FirebaseAuth auth;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

// To dismiss the dialog


        email = findViewById(R.id.fieldEmail);
        password = findViewById(R.id.fieldPassword);
        login = findViewById(R.id.btnL);
        auth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                String txt_email = email.getText().toString();
                String txt_pass = password.getText().toString();


                loginUser(txt_email, txt_pass);
            }
        });



    }

    private void loginUser(String txt_email, String txt_pass) {
        auth.signInWithEmailAndPassword(txt_email, txt_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //View rootView = activi.getWindow().getDecorView().findViewById(android.R.id.content);
              //  Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
                progress.dismiss();
            }

        });
    }
}