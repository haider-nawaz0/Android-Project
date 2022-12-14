package com.example.demofirebaseandroidapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Array;
import java.util.ArrayList;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText email, password;
    private Chip chipMale;


    public  static boolean isMale = true;


    public static TextInputEditText fBio, fieldUsername;
    public static String userLocation, txt_bio, txtUsername, txt_email, txt_password;

    private MaterialButton next,btnMoveToLoginScreen;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> arrayAdapter;
    private String[] items = {"Gujranwala", "Lahore", "Pindi", "Islamabad", "Karachi", "Sialkot", "Daska"};
    private FirebaseAuth auth;
    private ProgressDialog progress;
    private Uri imageUri;
    public static String imageDownloadLink;
    private ImageView profileImageView;
    ActivityResultLauncher<String> activityResultLauncher;


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



        profileImageView = findViewById(R.id.profileImageView);



        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch("image/*");
            }
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null){
                    profileImageView.setImageURI(result);
                    imageUri = result;

                }
            }
        });

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
               // Toast.makeText(RegisterActivity.this, userLocation, Toast.LENGTH_SHORT).show();
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

                txt_email = email.getText().toString();
                txt_password = password.getText().toString();

                //Get other values
                txt_bio = fBio.getText().toString();
                txtUsername = fieldUsername.getText().toString();


                if(imageUri == null || txt_email.isEmpty() || txt_password.isEmpty() || txt_bio.isEmpty()
                        || txtUsername.isEmpty() || userLocation.isEmpty()){
                    progress.hide();

                    Snackbar.make( findViewById(android.R.id.content), "Please input all the fields!", Snackbar.LENGTH_LONG).setBackgroundTint(getResources().getColor(R.color.red))
                            .show();

                    return;
                }


                    progress.show();

                    uploadPostImage();




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

                   }
                   else {
                       progress.hide();
                       Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
                }
            });


    }

    private void uploadPostImage(){

        if(imageUri != null){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference().child("profiles/"+ UUID.randomUUID().toString());

            storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //Toast.makeText(getContext(), "Image Uplaoded", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageDownloadLink  = uri.toString();



                                registerUser(txt_email, txt_password);

                                progress.hide();

                                Snackbar.make(findViewById(android.R.id.content), "Profile image Uploaded", Snackbar.LENGTH_LONG)
                                        .show();

                                startActivity(new Intent(getApplicationContext(), InterestsActivity.class));


                            }
                        });


                    }else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}