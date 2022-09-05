package com.example.demofirebaseandroidapp;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    private MaterialButton btnAddPost;
    private FirebaseFirestore db;
    private EditText fieldPost;
    private FirebaseUser user;
    private ProgressDialog progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        btnAddPost = view.findViewById(R.id.btnAddPost);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                 String txtPost = fieldPost.getText().toString();
                 addPostToFirestore(txtPost, user.getEmail());
            }
        });
        return view;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // save views as variables in this method
        // "view" is the one returned from onCreateView
        progress = new ProgressDialog(view.getContext());
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disabl

        fieldPost =    (EditText) view.findViewById(R.id.fieldPost);
    }

    private void addPostToFirestore(String caption, String email) {
        Map<String, Object> data = new HashMap<>();
        data.put("addedBy", email);
        data.put("caption", caption);
        data.put("likes", 99);
        data.put("createdAt", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
        //data.put("born", 1815);

        db.collection("posts")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress.hide();

                        Toast.makeText(getContext(), "Success, Post Uploaded.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progress.hide();

                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();

                    }
                });


    }

}