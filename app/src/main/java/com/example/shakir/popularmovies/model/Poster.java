package com.example.shakir.popularmovies.model;


public class Poster {

    private int mMovieId;
    private String mTitle;
    private  String mPosterpath;

    public Poster(int id, String title, String posterpath){

        mMovieId = id;
        mTitle = title;
        mPosterpath = posterpath;
    }

    public String getFinalPosterPath(){
        String link = "http://image.tmdb.org/t/p/";
        String size = "w185";
        //Other available options "w92", "w154", "w185", "w342", "w500", "w780", or "original"
        String seprator = "/";

        return link + size + seprator + mPosterpath;
    }

    public int getMovieId() {
        return mMovieId;
    }

    public void setMovieId(int movieId) {
        mMovieId = movieId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPosterpath() {
        return mPosterpath;
    }

    public void setPosterpath(String posterpath) {
        mPosterpath = posterpath;
    }
}
