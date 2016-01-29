package com.example.kbarbosa.myapp.model;

/**
 * Created by kbarbosa on 11/01/2016.
 */
public class Biography {

    private String text;
    private boolean truncated;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getTruncated() {
        return truncated;
    }

    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }
}
