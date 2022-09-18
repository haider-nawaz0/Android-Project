package com.example.demofirebaseandroidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.BreakIterator;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private boolean isLikeBtnClicked = false;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public PostAdapter(ArrayList<PostCard> posts, FirebaseFirestore db) {

        this.posts = posts;
        this.db = db;
    }

    ArrayList<PostCard> posts;

    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcard, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        holder.caption.setText(posts.get(position).getCaption());
        holder.likes.setText(posts.get(position).getLikes()+"");
        holder.time.setText(posts.get(position).getCreatedAt());
        holder.cardUsername.setText(posts.get(position).getUsername());

        holder.btnDelPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Post.")
                        .setMessage("Confirm to delete this post?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("posts").document(posts.get(position).getDocId().toString())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                posts.remove(posts.get(position));
                                                notifyItemRemoved(holder.getAdapterPosition());


                                                Toast.makeText(holder.caption.getContext(), "Post Deleted.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
                                            }
                                        });
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        holder.btnDelPost.setEnabled(false);
        holder.btnDelPost.setVisibility(View.GONE);

        if(posts.get(position).getAddedBy().equals(user.getEmail().toString())){
            holder.btnDelPost.setVisibility(View.VISIBLE);
            holder.btnDelPost.setEnabled(true);

        }


        if(posts.get(position).getImageLink() == null){

            holder.postImage.requestLayout();
            holder.postImage.getLayoutParams().height = 0;
        }

        Picasso.get().load(posts.get(position).getImageLink()).fit().centerCrop().into(holder.postImage);


        //Set the profile image for posts
        Picasso.get().load(posts.get(position).getAuthorImageLink()).fit().centerCrop().into(holder.authorImage);


        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              isLikeBtnClicked = !isLikeBtnClicked;

                if(isLikeBtnClicked){
                    int curr_likes = posts.get(position).getLikes();
                    curr_likes++;
                    posts.get(position).setLikes(curr_likes);

//                    Toast.makeText(view.getContext(), Integer.toString(curr_likes), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(view.getContext(), posts.get(position).getDocId().toString(), Toast.LENGTH_SHORT).show();
                    //Increase the like count in firestore
                    db.collection("posts")
                            .document(posts.get(position).getDocId()).update("likes", curr_likes)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    //Toast.makeText(view.getContext(), "Like updated", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });



                    holder.btnLike.setIcon(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_baseline_favorite_24));

           } else
           {
               int curr_likes = posts.get(position).getLikes();
               curr_likes--;
               posts.get(position).setLikes(curr_likes);

//                    Toast.makeText(view.getContext(), Integer.toString(curr_likes), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(view.getContext(), posts.get(position).getDocId().toString(), Toast.LENGTH_SHORT).show();
               //Increase the like count in firestore
               db.collection("posts")
                       .document(posts.get(position).getDocId()).update("likes", curr_likes)
                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void aVoid) {
                               Log.d(TAG, "DocumentSnapshot successfully updated!");
                               Toast.makeText(view.getContext(), "Like updated", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.w(TAG, "Error updating document", e);
                           }
                       });
               holder.btnLike.setIcon(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_baseline_favorite_border_24));
           }
            }
        });
    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


        //Views in the card
        TextView caption, likes, time, cardUsername;
        MaterialButton btnLike, btnDelPost;
        ImageView postImage, authorImage;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.cardCaption);
            likes = itemView.findViewById(R.id.likes);
            time = itemView.findViewById(R.id.cardTime);
            cardUsername = itemView.findViewById(R.id.cardUsername);
            btnLike = itemView.findViewById(R.id.btnLike);
            postImage = itemView.findViewById(R.id.postImage);
            btnDelPost = itemView.findViewById(R.id.btnDelPost);
            authorImage = itemView.findViewById(R.id.authorImage);
        }
    }

}
