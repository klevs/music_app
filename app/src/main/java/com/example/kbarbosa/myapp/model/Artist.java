package com.example.kbarbosa.myapp.model;

/**
 * Created by kbarbosa on 4/01/2016.
 */
public class Artist {

    private String name;
    private Biography[] biographies;
    private ArtistImage[] images;
    private Song[]  songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Biography[] getBiographies() {
        return biographies;
    }

    public void setBiographies(Biography[] biographies) {
        this.biographies = biographies;
    }

    public ArtistImage[] getImages() {
        return images;
    }

    public void setImages(ArtistImage[] images) {
        this.images = images;
    }

    public Song[] getSongs() {
        return songs;
    }

    public void setSongs(Song[] songs) {
        this.songs = songs;
    }
}
