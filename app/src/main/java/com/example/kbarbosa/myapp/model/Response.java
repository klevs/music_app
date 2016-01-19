package com.example.kbarbosa.myapp.model;

/**
 * Created by kbarbosa on 8/01/2016.
 */
public class Response {
    private Status status;
    private Artist[] artists;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public void setArtists(Artist[] artists) {
        this.artists = artists;
    }
}
