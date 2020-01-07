package com.example.myapplication;

import android.graphics.drawable.Drawable;

public class CardItem {
    String name;
    String post;
    Drawable image;

    public CardItem(String name, String post, Drawable image){
        this.name = name;
        this.post = post;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
