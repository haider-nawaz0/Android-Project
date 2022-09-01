package com.example.demofirebaseandroidapp;

public class PostCard {

    private String caption;
    private int likes;
    private String addedBy;  //email of the user who added it.
    private String createdAt;


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

    public PostCard(String caption, int likes, String addedBy, String createdAt) {
        this.caption = caption;
        this.likes = likes;
        this.addedBy = addedBy;
        this.createdAt = createdAt;
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
