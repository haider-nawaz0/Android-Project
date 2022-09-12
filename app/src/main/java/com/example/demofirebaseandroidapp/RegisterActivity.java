package com.example.demofirebaseandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Array;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText email, password;
    private Chip chipMale;


    public  static boolean isMale = true;


    public static TextInputEditText fBio, fieldUsername;
    public static String userLocation, txt_bio, txtUsername, txt_email;

    private MaterialButton next,btnMoveToLoginScreen;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> arrayAdapter;
    private String[] items = {"Gujranwala", "Lahore", "Pindi", "Islamabad"};
    private FirebaseAuth auth;
    private ProgressDialog progress;

    public static ArrayList<String> userProfileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnMoveToLoginScreen = findViewById(R.id.btnMoveToLoginScreen);
        fBio = findViewById(R.id.fBio);
        fieldUsername = findViewById(R.id.fieldUsername);


        userProfileData = new ArrayList<>();
        chipMale = findViewById(R.id.chipMale);

        btnMoveToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        chipMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    chipMale.setText("Female");
                    isMale = false;
                }else {
                    chipMale.setText("Male");
                    isMale= true;
                }
            }
        });


        //Dropdown stuff

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, items);
        autoCompleteTextView.setAdapter(arrayAdapter);


        //Handle drop down click
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                userLocation = item.toString();
                Toast.makeText(RegisterActivity.this, userLocation, Toast.LENGTH_SHORT).show();
            }
        });

        email = (TextInputEditText) findViewById(R.id.fEmail);
        password = (TextInputEditText) findViewById(R.id.fPassword);
        next = findViewById(R.id.btnNext);

        auth = FirebaseAuth.getInstance();

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    progress.show();

                    //Register the user
                    txt_email = email.getText().toString();
                    String txt_password = password.getText().toString();

                    //Get other values
                     txt_bio = fBio.getText().toString();
                     txtUsername = fieldUsername.getText().toString();

                    registerUser(txt_email, txt_password);


                startActivity(new Intent(getApplicationContext(), InterestsActivity.class));
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
                       startActivity(new Intent(getApplicationContext(), InterestsActivity.class));
                   }
                   else {
                       progress.hide();
                       Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
                }
            });


    }
}