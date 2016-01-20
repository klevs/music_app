package com.example.kbarbosa.myapp.model;

/**
 * Created by kbarbosa on 12/01/2016.
 */
public enum Bucket {

    BIOGRAPHY("biographies"),
    IMAGE("images"),
    SONG("songs");

    public final String text;

    Bucket(String text) {
        this.text = text;
    }
}
