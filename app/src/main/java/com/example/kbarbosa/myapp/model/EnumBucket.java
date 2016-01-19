package com.example.kbarbosa.myapp.model;

/**
 * Created by kbarbosa on 12/01/2016.
 */
public class EnumBucket {

    RequestBucket requestBucket;
    String bucket;

    public enum RequestBucket{
        biography, image, song
    }

    public EnumBucket(RequestBucket requestBucket){
        this.requestBucket = requestBucket;
    }

    public void caseBucket(){
        switch (requestBucket){
            case biography:
                bucket = "biographies";
                break;

            case image:
                bucket = "images";
                break;

            case song:
                bucket = "songs";
                break;

            default:
                break;
        }
    }
}
