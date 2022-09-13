package com.example.demofirebaseandroidapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
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



        holder.caption.setText(posts.get(position).getCaption());
        holder.likes.setText(posts.get(position).getLikes()+"");
        holder.time.setText(posts.get(position).getCreatedAt());
        holder.cardEmail.setText(posts.get(position).getAddedBy());

        Picasso.get().load(posts.get(position).getImageLink()).fit().centerCrop().into(holder.postImage);


//        //Check if the current user likes the post
//        for(PostCard i: posts){
//            Toast.makeText(holder.caption.getContext(), i.getLikedBy().get(0)+"", Toast.LENGTH_SHORT).show();
//            for(String email: i.getLikedBy()){
//                if(MainActivity.user.getEmail().toString().equals(email)){
//                    isLikeBtnClicked = true;
//                    Toast.makeText(holder.caption.getContext(), "You have liked a post already", Toast.LENGTH_SHORT).show();
//                    holder.btnLike.setIcon(ContextCompat.getDrawable(holder.btnLike.getContext(),R.drawable.ic_baseline_favorite_24));
//
//                }else {
//                    isLikeBtnClicked = false;
//                }
//            }
//        }



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
                                    Toast.makeText(view.getContext(), "Like updated", Toast.LENGTH_SHORT).show();
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
        TextView caption, likes, time, cardEmail;
        MaterialButton btnLike;
        ImageView postImage;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.cardCaption);
            likes = itemView.findViewById(R.id.likes);
            time = itemView.findViewById(R.id.cardTime);
            cardEmail = itemView.findViewById(R.id.cardEmail);
            btnLike = itemView.findViewById(R.id.btnLike);
            postImage = itemView.findViewById(R.id.postImage);
        }
    }

}
