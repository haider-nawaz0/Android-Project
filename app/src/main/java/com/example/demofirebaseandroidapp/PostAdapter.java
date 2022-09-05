package com.example.demofirebaseandroidapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.button.MaterialButton;

import java.text.BreakIterator;
import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
    private boolean isLikeBtnClicked = false;

    public PostAdapter(ArrayList<PostCard> posts) {

        this.posts = posts;
    }

    ArrayList<PostCard> posts;



    @NonNull
    @Override
    public PostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcard, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.MyViewHolder holder, int position) {

        holder.caption.setText(posts.get(position).getCaption());
        holder.likes.setText(posts.get(position).getLikes()+"");
        holder.time.setText(posts.get(position).getCreatedAt());
        holder.cardEmail.setText(posts.get(position).getAddedBy());

        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               isLikeBtnClicked = !isLikeBtnClicked;

                if(isLikeBtnClicked){

                    //Increase the like count in firestore

                    holder.btnLike.setIcon(ContextCompat.getDrawable(view.getContext(),R.drawable.ic_baseline_favorite_24));

           } else
           {
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



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            caption = itemView.findViewById(R.id.cardCaption);
            likes = itemView.findViewById(R.id.likes);
            time = itemView.findViewById(R.id.cardTime);
            cardEmail = itemView.findViewById(R.id.cardEmail);
            btnLike = itemView.findViewById(R.id.btnLike);
        }
    }

}
