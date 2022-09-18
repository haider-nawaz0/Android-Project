package com.example.demofirebaseandroidapp;

import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;

public class PostCard {

    private String caption;
    private String username;
    private int likes;
    private String addedBy;  //email of the user who added it.
    private String createdAt;
    private String docId;
    private String imageLink, authorImageLink;


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

    public String getAuthorImageLink() {
        return authorImageLink;
    }

    public void setAuthorImageLink(String authorImageLink) {
        this.authorImageLink = authorImageLink;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public PostCard(String caption, int likes, String addedBy, String createdAt, String docId, ArrayList<String> likedBy, String imageLink, String authorImageLink, String username) {
        this.caption = caption;
        this.likes = likes;
        this.addedBy = addedBy;
        this.createdAt = createdAt;
        this.docId = docId;
        this.likedBy = likedBy;
        this.imageLink = imageLink;
        this.authorImageLink = authorImageLink;
        this.username = username;
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
