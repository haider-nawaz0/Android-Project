package com.example.demofirebaseandroidapp;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    private MaterialButton btnAddPost;
    private FirebaseFirestore db;
    private EditText fieldPost;
    private FirebaseUser user;
    private ProgressDialog progress;
    private List<String> likedBy;
    private ImageView imageView;
    ActivityResultLauncher<String> activityResultLauncher;
    private Uri imageUri;
    private String imageDownloadLink;
    private FirebaseAuth  auth;

    public static String currUserImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        btnAddPost = view.findViewById(R.id.btnAddPost);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        likedBy = new ArrayList<String>();
        auth = FirebaseAuth.getInstance();



        getCurrUserProfileImage();

        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
               uploadPostImage();

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
        progress.setCancelable(false);

        fieldPost =    (EditText) view.findViewById(R.id.fieldPost);
        imageView = view.findViewById(R.id.imageView);




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch("image/*");
            }
        });


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null){
                    imageView.setImageURI(result);
                    imageUri = result;

                }
            }
        });



    }

    private void addPostToFirestore(String caption, String email) {

        Map<String, Object> data = new HashMap<>();
        data.put("addedBy", email);
        data.put("caption", caption);
        data.put("likes", 0);
        data.put("createdAt", new SimpleDateFormat("yyyy/MM/dd HH:mm").format(Calendar.getInstance().getTime()));
        data.put("likedBy",likedBy);
        data.put("imageLink", imageDownloadLink);
        data.put("authorImageLink", currUserImage);
        data.put("username", LoginActivity.currUsername);


        db.collection("posts")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress.hide();

                        dismiss();

                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Post Uploaded", Snackbar.LENGTH_LONG)
                                .show();
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

    private void getCurrUserProfileImage(){


        db = FirebaseFirestore.getInstance();


        db.collection("profiles").whereEqualTo("email", auth.getCurrentUser().getEmail().toString()).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                currUserImage = document.getString("profileImageLink");
                               // Toast.makeText(getContext(), currUserImage, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }
        );

    }

    private void uploadPostImage(){

        if(imageUri != null){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference().child("images/"+ UUID.randomUUID().toString());

            storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        //Toast.makeText(getContext(), "Image Uplaoded", Toast.LENGTH_SHORT).show();
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageDownloadLink  = uri.toString();

                                //Add other data
                                String txtPost = fieldPost.getText().toString();
                                addPostToFirestore(txtPost, user.getEmail());

                            }
                        });


                    }else {
                        Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        else {
            //Add other data
            String txtPost = fieldPost.getText().toString();
            addPostToFirestore(txtPost, user.getEmail());
        }



    }


}