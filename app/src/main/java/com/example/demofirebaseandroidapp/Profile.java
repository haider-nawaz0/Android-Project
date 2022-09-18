package com.example.demofirebaseandroidapp;

import java.util.ArrayList;

public class Profile {

    private String username, location, joined, email, bio ;
    private boolean male;
    private ArrayList<String> interests;
    private String profileImageLink;

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }

    public String getProfileImageLink() {
        return profileImageLink;
    }

    public void setProfileImageLink(String profileImageLink) {
        this.profileImageLink = profileImageLink;
    }

    public Profile() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }



    public Profile(String username, String location, String joined, String email, String bio, boolean male, ArrayList<String> interests, String profileImageLink) {
        this.username = username;
        this.location = location;
        this.joined = joined;
        this.email = email;
        this.bio = bio;
        this.male = male;
        this.interests = interests;
        this.profileImageLink = profileImageLink;
    }
}
