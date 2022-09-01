package com.example.demofirebaseandroidapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.demofirebaseandroidapp.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView txtEmail;

    private FirebaseFirestore db;
    private ProgressDialog progress;
    private MaterialButton btnCreatePost, btnSignOut;

    RecyclerView recyclerView;
    ArrayList<PostCard> posts;
    PostAdapter postAdapter;


    //BottomNavigationView bottomNavigation;
    ActivityMainBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Fetching Posts...");
        progress.show();

        ProgressDialog progressSignout = new ProgressDialog(view.getContext());
        progressSignout.setCancelable(false);
        progressSignout.setMessage("Signing out...");










        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        posts = new ArrayList<PostCard>();
        postAdapter = new PostAdapter(posts);

        recyclerView.setAdapter(postAdapter);

        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();
        EventChangeListener();



        btnCreatePost = view.findViewById(R.id.btnCreatePost);
        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(HomeActivity.this, "yey", Toast.LENGTH_SHORT).show();
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
               bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        btnSignOut = view.findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressSignout.show();
                auth.signOut();
                progressSignout.dismiss();
                startActivity(new Intent(view.getContext(), MainActivity.class));
               getActivity().finish();
            }
        });



    }
    private void EventChangeListener() {
        db.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    progress.dismiss();
                    Log.e("firestore error", error.getMessage());
                    return;
                }

                for(DocumentChange dc: value.getDocumentChanges()){
                    if(dc.getType() == DocumentChange.Type.ADDED){
                        posts.add(dc.getDocument().toObject(PostCard.class));
                    }

                    postAdapter.notifyDataSetChanged();
                    progress.dismiss();
                }
            }
        });
    }



}