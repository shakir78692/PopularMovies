package com.example.shakir.popularmovies;

/**
 * Created by Shakir on 17/08/15.
 */
public class Movie {
    int mMovieId;
    String mTitle;
    String mPosterPath;
    String mBackDropPath;
    String mOverview;
    String mReleaseDate;
    Trailer[] mTrailers;
    String mImdbId;
    String mHomePage;
    double mVoteCount;
    double mVoteAverage;
    double mBudget;
    double mRevenue;

    public Movie(int movieId, String title, String posterPath, String backDropPath, String overview, String releaseDate, Trailer[] trailers, String imdbId, String homePage, double voteCount, double voteAverage, double budget, double revenue) {
        mMovieId = movieId;
        mTitle = title;
        mPosterPath = posterPath;
        mBackDropPath = backDropPath;
        mOverview = overview;
        mReleaseDate = releaseDate;
        mTrailers = trailers;
        mImdbId = imdbId;
        mHomePage = homePage;
        mVoteCount = voteCount;
        mVoteAverage = voteAverage;
        mBudget = budget;
        mRevenue = revenue;
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

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackDropPath() {
        return mBackDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        mBackDropPath = backDropPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public Trailer[] getTrailers() {
        return mTrailers;
    }

    public void setTrailers(Trailer[] trailers) {
        mTrailers = trailers;
    }

    public String getImdbId() {
        return mImdbId;
    }

    public void setImdbId(String imdbId) {
        mImdbId = imdbId;
    }

    public String getHomePage() {
        return mHomePage;
    }

    public void setHomePage(String homePage) {
        mHomePage = homePage;
    }

    public double getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(double voteCount) {
        mVoteCount = voteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public double getBudget() {
        return mBudget;
    }

    public void setBudget(double budget) {
        mBudget = budget;
    }

    public double getRevenue() {
        return mRevenue;
    }

    public void setRevenue(double revenue) {
        mRevenue = revenue;
    }
}
