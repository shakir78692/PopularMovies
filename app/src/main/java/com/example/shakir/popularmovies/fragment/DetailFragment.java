package com.example.shakir.popularmovies.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shakir.popularmovies.R;
import com.example.shakir.popularmovies.Utility;
import com.example.shakir.popularmovies.adapter.GridAdapter;
import com.example.shakir.popularmovies.data.MovieContract;
import com.example.shakir.popularmovies.model.Movie;
import com.example.shakir.popularmovies.model.Review;
import com.example.shakir.popularmovies.model.Trailer;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DetailFragment extends Fragment {

    private static final String LOG = DetailFragment.class.getSimpleName();
    private static final String SHARE_HASHTAG = " #Popular Movies App";
    private boolean mFavourated = false;
    private ShareActionProvider mShareActionProvider;
    private Movie mMovie;
    private String mShareStr;

    ImageView backdropView;
    ImageView posterView;
    TextView title;
    TextView overview;
    TextView runtimeView;
    TextView yearView;
    TextView ratingView;
    ImageView fav;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);


        Intent intent = getActivity().getIntent();

        long movie_id = intent.getLongExtra("MOVIE_ID", 135397);

        backdropView = (ImageView) rootView.findViewById(R.id.backdropView);
        posterView = (ImageView) rootView.findViewById(R.id.posterView);
        title = (TextView) rootView.findViewById(R.id.titleView);
        overview = (TextView) rootView.findViewById(R.id.overview);
        runtimeView = (TextView) rootView.findViewById(R.id.runtimeView);
        yearView = (TextView) rootView.findViewById(R.id.yearView);
        ratingView = (TextView) rootView.findViewById(R.id.ratingView);
        fav = (ImageView) rootView.findViewById(R.id.favButton);

        getMovie(movie_id);


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_frag, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mMovie != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        mShareStr ="Check out "+mMovie.getTitle() + "Trailer on youtu.be/"+mMovie.getTrailers()[0].getSource();
        shareIntent.putExtra(Intent.EXTRA_TEXT, mShareStr+SHARE_HASHTAG);
        return shareIntent;
    }

    private void updateDisplay(final Movie movie) {


        Picasso.with(getActivity()).load(movie.getFinalBackdropPath()).placeholder(R.drawable.backdrop_placeholder).
                fit().centerInside().into(backdropView);

        Picasso.with(getActivity()).load(movie.getFinalPosterPath()).placeholder(R.drawable.poster_placeholder)
                .fit().centerInside().into(posterView);

        title.setText(movie.getTitle());
        overview.setText(movie.getOverview());
        runtimeView.setText(movie.getRuntime() + " mins");
        yearView.setText(movie.getYear());
        ratingView.setText(movie.getVoteAverage() + "/10");

        backdropView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movie.getTrailers()[0].getSource()));
                startActivity(intent);
            }
        });


        if (GridAdapter.isFavourite(movie.getMovieId(),getActivity())){

            mFavourated = true;
        }

        if (mFavourated){

            fav.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actionFavourite(movie, (ImageView) v, getActivity());

            }
        });

    }

    public static void actionFavourite(Movie movie, ImageView view, Context context) {

        long movie_id;

        Cursor cursor = context.getContentResolver().query(
                MovieContract.MoviesEntry.CONTENT_URI,
                new String[]{MovieContract.MoviesEntry.COLUMN_MOVIE_ID},
                MovieContract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getMovieId())},
                null
        );

        if (cursor.moveToFirst()){

            context.getContentResolver().delete(
                    MovieContract.MoviesEntry.CONTENT_URI,
                    MovieContract.MoviesEntry.COLUMN_MOVIE_ID + "=?",
                    new String[]{String.valueOf(movie.getMovieId())}
            );

            view.setImageResource(R.drawable.ic_favorite_border_black_24dp);

            Toast.makeText(context,"Removed",Toast.LENGTH_SHORT).show();

        }
        else {

            ContentValues values = new ContentValues();
            values.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            values.put(MovieContract.MoviesEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieContract.MoviesEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            values.put(MovieContract.MoviesEntry.COLUMN_BACKDROP_PATH, movie.getBackDropPath());
            values.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(MovieContract.MoviesEntry.COLUMN_TAGLINE, movie.getTagline());
            values.put(MovieContract.MoviesEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            values.put(MovieContract.MoviesEntry.COLUMN_RUNTIME, movie.getRuntime());
            values.put(MovieContract.MoviesEntry.COLUMN_REVIEWS, Utility.reviewsToString(movie.getReviews()));
            values.put(MovieContract.MoviesEntry.COLUMN_TRAILERS, Utility.trailersToString(movie.getTrailers()));
            values.put(MovieContract.MoviesEntry.COLUMN_IMDB_ID, movie.getImdbId());
            values.put(MovieContract.MoviesEntry.COLUMN_HOMEPAGE, movie.getHomePage());
            values.put(MovieContract.MoviesEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
            values.put(MovieContract.MoviesEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());

            Uri uri = context.getContentResolver().insert(MovieContract.MoviesEntry.CONTENT_URI,
                    values);

            view.setImageResource(R.drawable.ic_favorite_black_24dp);

            Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void getMovie(long id) {

        final Movie[] movie = new Movie[1];
        String api_key = "199529fbee9c95fdf96c91ea80f2452e";
        final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        long movie_id = id;
        final String API_KEY = "api_key";
        final String APP_TO_RES = "append_to_response";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movie_id + "?")
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(APP_TO_RES, "trailers,reviews")
                .build();


        if (isNetworkAvailable()) {

            Log.d(LOG, "Internet Connection Available");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.valueOf(uri)).build();

            Log.d(LOG, uri.toString());

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.d(LOG, "Request Failed");

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String jsonData = response.body().string();

                    if (response.isSuccessful()) {

                        mMovie = parseMovie(jsonData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateDisplay(mMovie);
                            }
                        });

                    }

//                    Log.d(LOG, jsonData);
                }


            });

        } else {

            Log.d(LOG, "No Internet Connection");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.general_error_title)
                    .setMessage(R.string.no_internet_error)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private Movie parseMovie(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            int movieId = jsonObject.getInt("id");
            String title = jsonObject.getString("title");
            String posterPath = jsonObject.getString("poster_path");
            String backDropPath = jsonObject.getString("backdrop_path");
            String overview = jsonObject.getString("overview");
            String tagline = jsonObject.getString("tagline");
            String releaseDate = jsonObject.getString("release_date");
            String imdbId = jsonObject.getString("imdb_id");
            String homePage = jsonObject.getString("homepage");
            double voteCount = jsonObject.getDouble("vote_count");
            double voteAverage = jsonObject.getDouble("vote_average");
            int runtime = jsonObject.getInt("runtime");

            JSONObject trailerObject = jsonObject.getJSONObject("trailers");
            JSONArray youtubeArray = trailerObject.getJSONArray("youtube");
            Trailer[] trailers = new Trailer[youtubeArray.length()];
            for (int i = 0; i < youtubeArray.length(); i++) {

                JSONObject trailerjson = youtubeArray.getJSONObject(i);

                String name = trailerjson.getString("name");
                String source = trailerjson.getString("source");
                String type = trailerjson.getString("type");

                Trailer trailer = new Trailer(name, source, type);
                trailers[i] = trailer;
            }

            JSONObject reviewObject = jsonObject.getJSONObject("reviews");
            JSONArray reviewArray = reviewObject.getJSONArray("results");
            Review[] reviews = new Review[reviewArray.length()];
            for (int i =0; i < reviewArray.length(); i++){

                JSONObject reviewJson = reviewArray.getJSONObject(i);
                String author = reviewJson.getString("author");
                String content = reviewJson.getString("content");

                Review review = new Review(author,content);
                reviews[i] = review;
            }

            Movie movie = new Movie(movieId, title, posterPath, backDropPath, overview,tagline, releaseDate,
                    runtime, reviews, trailers, imdbId, homePage, voteCount, voteAverage);

            Log.d(LOG, movieId + " " + title + " " + posterPath + " " + backDropPath + " " + overview
                    + " " + releaseDate + " " + tagline+" "+Utility.reviewsToString(reviews)+" "
                    + Utility.trailersToString(trailers)+ imdbId + " " + homePage + " "
                    + voteCount + " " + voteAverage);

            return movie;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(LOG, "Parse Movie Failed");
            return null;
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {

            isAvailable = true;
        }

        return isAvailable;
    }
}
