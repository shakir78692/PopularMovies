package com.example.shakir.popularmovies;

import android.util.Log;

import com.example.shakir.popularmovies.model.Review;
import com.example.shakir.popularmovies.model.Trailer;

/**
 * Created by Shakir on 18/09/15.
 */
public class Utility {

    public static String trailersToString(Trailer[] trailers){

        String retString = "";

        for (Trailer trailer : trailers){

            retString = retString + trailer.getName()+"-"+trailer.getSource()+"-"+trailer.getType()+"&&";
        }
        Log.i("UTILITY",retString);
        return retString;
    }

    public static Trailer[] stringToTrailers(String string){
        Trailer[] retTrailers = null;

        String[] trailerString = string.split("&&");
        for (int i = 0; i < trailerString.length; i++){

            String[] constructor = trailerString[i].split("-");
            Trailer trailer = new Trailer(constructor[0],constructor[1],constructor[2]);
            retTrailers[i] = trailer;
        }

        return retTrailers;
    }

    public static String reviewsToString(Review[] reviews){

        String retString = "";

        for (Review review : reviews){

            retString = retString + review.getAuthor()+"-"+review.getContent()+"&&";
        }
        Log.i("UTILITY",retString);
        return retString;
    }

    public static Review[] stringToReview(String string){
        Review[] retReviews = null;
        String[] reviewString = string.split("&&");
        for (int i =0; i < reviewString.length; i++){
            String[] contructor = reviewString[i].split("-");
            Review review = new Review(contructor[0], contructor[1]);
            retReviews[i] =review;
        }

        return retReviews;
    }

}
