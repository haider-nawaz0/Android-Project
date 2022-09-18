package com.example.demofirebaseandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private MaterialButton login, btnMoveToSignUpScreen;
    private FirebaseAuth auth;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);

        // disable dismiss by tapping outside of the dialog

        email = findViewById(R.id.fieldEmail);
        password = findViewById(R.id.fieldPassword);
        login = findViewById(R.id.btnL);
        btnMoveToSignUpScreen = findViewById(R.id.btnMoveToSignUpScreen);
        auth = FirebaseAuth.getInstance();


        btnMoveToSignUpScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_pass = password.getText().toString();


                if(txt_email.isEmpty() || txt_pass.isEmpty()){

                    Snackbar.make( findViewById(android.R.id.content), "Please input all the fields!", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.red))
                            .show();

                    return;
                }
                progress.show();


                loginUser(txt_email, txt_pass);
            }
        });
    }

    private void loginUser(String txt_email, String txt_pass) {
        auth.signInWithEmailAndPassword(txt_email, txt_pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


                progress.dismiss();

                //Once we get the profile image link and set it to the static var, then we move to the next screen
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();

                //getCurrUsername();



            }

        });
    }


}