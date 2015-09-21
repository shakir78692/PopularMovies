package com.example.shakir.popularmovies.model;


public class Trailer {

    String mName;
    String mSource;
    String mType;

    public Trailer(String name, String source, String type) {
        mName = name;
        mSource = source;
        mType = type;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

}
