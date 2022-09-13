package com.example.demofirebaseandroidapp;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class PostCard {

    private String caption;
    private int likes;
    private String addedBy;  //email of the user who added it.
    private String createdAt;
    private String docId;
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    private ArrayList<String> likedBy;

    public ArrayList<String> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(ArrayList<String> likedBy) {
        this.likedBy = likedBy;
    }

    @DocumentId
    public String getDocId() {
        return docId;
    }
    @DocumentId
    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PostCard() {

    }

    public PostCard(String caption, int likes, String addedBy, String createdAt, String docId, ArrayList<String> likedBy, String imageLink) {
        this.caption = caption;
        this.likes = likes;
        this.addedBy = addedBy;
        this.createdAt = createdAt;
        this.docId = docId;
        this.likedBy = likedBy;
        this.imageLink = imageLink;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
