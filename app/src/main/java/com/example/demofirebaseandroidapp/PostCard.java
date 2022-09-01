package com.example.demofirebaseandroidapp;

public class PostCard {

    private String post;
    private int likes;




    public PostCard() {

    }

    public PostCard(String post, int likes) {
        this.post = post;
        this.likes = likes;

    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String caption) {
        this.post = post;
    }
}
