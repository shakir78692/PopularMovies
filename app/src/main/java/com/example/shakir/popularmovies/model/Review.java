package com.example.shakir.popularmovies.model;

/**
 * Created by Shakir on 19/09/15.
 */
public class Review {

    String mAuthor;
    String mContent;

    public Review(String author, String content){

        mAuthor = author;
        mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }
}
