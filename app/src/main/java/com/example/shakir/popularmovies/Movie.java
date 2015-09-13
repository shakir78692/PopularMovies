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
    String mTagline;
    String mReleaseDate;
    int mRuntime;
    Trailer[] mTrailers;
    String mImdbId;
    String mHomePage;
    double mVoteCount;
    double mVoteAverage;


    public Movie(int movieId, String title, String posterPath, String backDropPath, String overview,String tagline, String releaseDate, int runtime, Trailer[] trailers, String imdbId, String homePage, double voteCount, double voteAverage) {
        mMovieId = movieId;
        mTitle = title;
        mPosterPath = posterPath;
        mBackDropPath = backDropPath;
        mOverview = overview;
        mTagline = tagline;

        mReleaseDate = releaseDate;
        mRuntime = runtime;
        mTrailers = trailers;
        mImdbId = imdbId;
        mHomePage = homePage;
        mVoteCount = voteCount;
        mVoteAverage = voteAverage;

    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
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

    public int getRuntime() {
        return mRuntime;
    }

    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    public String getFinalPosterPath() {

        String link = "http://image.tmdb.org/t/p/";
        String size = "w185";
        //Other available options "w92", "w154", "w185", "w342", "w500", "w780", or "original"
        String seprator = "/";

        return link + size + mPosterPath;
    }

    public String getFinalBackdropPath() {

        String link = "http://image.tmdb.org/t/p/";
        String size = "w342";
        //Other available options "w92", "w154", "w185", "w342", "w500", "w780", or "original"
        String seprator = "/";

        return link + size + mBackDropPath;
    }

    public String getYear() {

        int index = mReleaseDate.indexOf('-');
        int start = index + 1;
        int end = index + 3;
        int month = Integer.parseInt(mReleaseDate.substring(start, end));
        String monthString;
        switch (month) {
            case 1:
                monthString = "January";
                break;
            case 2:
                monthString = "February";
                break;
            case 3:
                monthString = "March";
                break;
            case 4:
                monthString = "April";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "June";
                break;
            case 7:
                monthString = "July";
                break;
            case 8:
                monthString = "August";
                break;
            case 9:
                monthString = "September";
                break;
            case 10:
                monthString = "October";
                break;
            case 11:
                monthString = "November";
                break;
            case 12:
                monthString = "December";
                break;
            default:
                monthString = "Invalid month";
                break;
        }
        return monthString + " " + mReleaseDate.substring(0, 4);
    }
}
