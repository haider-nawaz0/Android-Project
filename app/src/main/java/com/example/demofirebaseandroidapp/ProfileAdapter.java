package com.example.demofirebaseandroidapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    public ProfileAdapter(ArrayList<Profile> profiles) {

        this.profiles = profiles;
    }

    ArrayList<Profile> profiles;

    @NonNull
    @Override
    public ProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card, parent, false);
        return new ProfileAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cardUsername.setText(profiles.get(position).getUsername());
        holder.cardLocation.setText(profiles.get(position).getLocation());
        holder.cardJoined.setText("joined on "+profiles.get(position).getJoined());
        holder.cardBio.setText(profiles.get(position).getBio());
        holder.cardGender.setText(profiles.get(position).isMale()? "M":"F");
        Picasso.get().load(profiles.get(position).getProfileImageLink()).fit().centerCrop().into(holder.profileImage);



       // holder.chipGroup.addView(new Chip(holder.itemView.getContext(),""));

        for(String chipTxt: profiles.get(position).getInterests()){
            Chip chip = new Chip(holder.itemView.getContext());
            chip.setText(chipTxt);
            holder.chipGroup.addView(chip);
        }

    }


    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //Views in the Profile card
        TextView cardUsername, cardLocation, cardJoined, cardBio, cardGender;
        MaterialButton profileIcon;
        ChipGroup chipGroup;
        ImageView profileImage;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardUsername = itemView.findViewById(R.id.cardUsername);
            cardLocation = itemView.findViewById(R.id.cardLocation);
            cardJoined = itemView.findViewById(R.id.cardJoined);
            cardBio = itemView.findViewById(R.id.bio);

            chipGroup = itemView.findViewById(R.id.interestsChipGroup);
            profileImage = itemView.findViewById(R.id.profileImage);
            cardGender = itemView.findViewById(R.id.cardGender);

           // btnLike = itemView.findViewById(R.id.btnLike);
        }
    }
}
