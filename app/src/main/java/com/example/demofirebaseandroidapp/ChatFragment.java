package com.example.demofirebaseandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private ProgressDialog progress;


    RecyclerView recyclerView, userProfileRecycleView;
    ArrayList<PostCard> posts;
    PostAdapter postAdapter;
    private ProfileAdapter profileAdapter;

    private ArrayList<Profile> profiles;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = new ProgressDialog(view.getContext());
        progress.setCancelable(false);
        progress.setMessage("Fetching Your Posts...");
        progress.show();



        recyclerView = view.findViewById(R.id.myPostsRecycleView);
        userProfileRecycleView = view.findViewById(R.id.YourProfileRecycleView);

        recyclerView.setHasFixedSize(true);
        userProfileRecycleView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userProfileRecycleView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        profiles = new ArrayList<Profile>();
        profileAdapter = new ProfileAdapter(profiles);

        userProfileRecycleView.setAdapter(profileAdapter);

        posts = new ArrayList<PostCard>();
        db = FirebaseFirestore.getInstance();
        postAdapter = new PostAdapter(posts, db);
        recyclerView.setAdapter(postAdapter);
        auth = FirebaseAuth.getInstance();
        EventChangeListener();
        ProfileChangeListener(auth.getCurrentUser().getEmail().toString());
    }


    private void EventChangeListener() {
        db.collection("posts").whereEqualTo("addedBy", auth.getCurrentUser().getEmail().toString()).orderBy("createdAt", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    private void ProfileChangeListener(String curr_email) {
        db.collection("profiles").whereEqualTo("email", curr_email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(!task.isSuccessful()){
                    progress.dismiss();
                    Toast.makeText(getView().getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }else {

                    for(QueryDocumentSnapshot document: task.getResult()){

                        //Toast.makeText(getView().getContext(), document.toObject(Profile.class).toString(), Toast.LENGTH_SHORT).show();
                        profiles.add(document.toObject(Profile.class));
                        profileAdapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                }
            }
        });


    }
}