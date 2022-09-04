package com.example.demofirebaseandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterestsActivity extends AppCompatActivity {

    private ChipGroup chipGroup;
    private MaterialButton btnAllDone;
    private FirebaseFirestore db;

    private ProgressDialog progress;

    private List<String> interests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false);


        db = FirebaseFirestore.getInstance();

        interests = new ArrayList<>();

        chipGroup = findViewById(R.id.chipGroup);
        btnAllDone = findViewById(R.id.btnAllDone);

        btnAllDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();

                //Get the user selected chips
                List<Integer> ids = chipGroup.getCheckedChipIds();
                for (Integer id:ids){
                    Chip chip = chipGroup.findViewById(id);
                    interests.add(chip.getText().toString());
                  //  Toast.makeText(getApplicationContext(), chip.getText().toString(), Toast.LENGTH_SHORT).show();
                }


                //save the profile data
                addProfileToFirestore(RegisterActivity.txtUsername, RegisterActivity.txt_email, RegisterActivity.txt_bio, RegisterActivity.userLocation, RegisterActivity.isMale);

            }
        });



    }
    private void addProfileToFirestore(String username, String email, String bio, String location, boolean isMale) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("location", location);
        data.put("bio", bio);
        data.put("male", isMale);
        data.put("interests", interests);
        data.put("joined", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
        data.put("email", email);

        db.collection("profiles")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress.hide();

                        Snackbar.make(findViewById(android.R.id.content), "Your Profile has been created", Snackbar.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.hide();

                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                });


    }
}