package com.example.demofirebaseandroidapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demofirebaseandroidapp.databinding.ActivityHomeBinding;
import com.example.demofirebaseandroidapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;

    public static String currUsername;
    private FirebaseAuth auth;


    //BottomNavigationView bottomNavigation;
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Navigation stuff
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        replaceFragment(new FeedFragment());
       // bottomNavigation = findViewById(R.id.bottomNavigation);

        auth = FirebaseAuth.getInstance();

        getCurrUsername();

        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.feed:{

                   replaceFragment(new FeedFragment());
                    break;
                }

                case R.id.search:{
                   replaceFragment(new SearchFragment());
                    break;
                }

                case R.id.chat:{
                    replaceFragment(new ChatFragment());
                    break;
                }
            }

            return true;
        });

    }

    private void replaceFragment(Fragment frag){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trx = manager.beginTransaction();
        trx.replace(R.id.relativeLayoutHome, frag);
        trx.commit();
    }

    private void getCurrUsername(){
        db = FirebaseFirestore.getInstance();
        db.collection("profiles").whereEqualTo("email", auth.getCurrentUser().getEmail().toString()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currUsername = document.getString("username");
                                Toast.makeText(getApplicationContext(), currUsername, Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }
        );

    }

}