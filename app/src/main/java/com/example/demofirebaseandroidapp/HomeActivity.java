package com.example.demofirebaseandroidapp;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
//    private FirebaseAuth auth;
//    private FirebaseUser user;
//    private TextView txtEmail;
//
//    private FirebaseFirestore db;
//    private ProgressDialog progress;
//    private MaterialButton btnCreatePost, btnSignOut;
//
//    RecyclerView recyclerView;
//    ArrayList<PostCard> posts;
//    PostAdapter postAdapter;


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







        binding.bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.feed:{

                   replaceFragment(new FeedFragment());
                    break;
                }

                case R.id.search:{
                  //  replaceFragment(new SearchFragment());
                    break;
                }

                case R.id.chat:{
                    replaceFragment(new ChatFragment());
                    break;
                }
            }

            return true;
        });

//
//        progress = new ProgressDialog(this);
//        progress.setCancelable(false);
//        progress.setMessage("Fetching Posts...");
//        progress.show();
//
//        ProgressDialog progressSignout = new ProgressDialog(this);
//        progressSignout.setCancelable(false);
//        progressSignout.setMessage("Signing out...");

//
//        recyclerView = findViewById(R.id.recycleView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        posts = new ArrayList<PostCard>();
//        postAdapter = new PostAdapter(posts);
//
//        recyclerView.setAdapter(postAdapter);
//
//        db = FirebaseFirestore.getInstance();
//
       // auth = FirebaseAuth.getInstance();
       // EventChangeListener();

//
//        btnCreatePost = findViewById(R.id.btnCreatePost);
//        btnCreatePost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               // Toast.makeText(HomeActivity.this, "yey", Toast.LENGTH_SHORT).show();
//                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//            }
//        });
//
//        btnSignOut = findViewById(R.id.btnSignOut);
//        btnSignOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progressSignout.show();
//                auth.signOut();
//                progressSignout.dismiss();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                finish();
//            }
//        });
      //  user = auth.getCurrentUser();
//
//        txtEmail = findViewById(R.id.txtEmail);
//
//
//        txtEmail.setText(user.getEmail().toString());
    }

//    private void EventChangeListener() {
//        db.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if(error != null){
//                    progress.dismiss();
//                    Log.e("firestore error", error.getMessage());
//                    return;
//                }
//
//                for(DocumentChange dc: value.getDocumentChanges()){
//                    if(dc.getType() == DocumentChange.Type.ADDED){
//                        posts.add(dc.getDocument().toObject(PostCard.class));
//                    }
//
//                    postAdapter.notifyDataSetChanged();
//                    progress.dismiss();
//                }
//            }
//        });
//    }

    private void replaceFragment(Fragment frag){

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trx = manager.beginTransaction();
        trx.replace(R.id.relativeLayoutHome, frag);
        trx.commit();
    }

}