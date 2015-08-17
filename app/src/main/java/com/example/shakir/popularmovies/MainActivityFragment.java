package com.example.shakir.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    final String LOG = MainActivityFragment.class.getSimpleName();

    Poster[] mPosters;
    GridAdapter gridAdapter;
    GridView gridView;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mPosters = new Poster[1];
        Poster poster = new Poster(198184,"abc","/7SGGUiTE6oc2fh9MjIk5M00dsQd.jpg");
        mPosters[0] = poster;
        gridAdapter = new GridAdapter(getActivity(),mPosters);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getMovie(id);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();

        UpdateDisplay();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void getPosters(String sort_by){

        String api_key = "199529fbee9c95fdf96c91ea80f2452e";

        final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
        final String SORT_BY = "sort_by";
        final String API_KEY = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY, sort_by)
                .appendQueryParameter(API_KEY, api_key)
                .build();


        if (isNetworkAvailable()){

            Log.d(LOG, "Internet Connection Available");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.valueOf(uri)).build();

            Log.d(LOG, uri.toString());

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.d(LOG,"Request Failed");

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String jsonData = response.body().string();

                    if (response.isSuccessful()) {

                        final Poster[] posters = ParsePosters(jsonData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                UpdateAdapter(posters);
                            }
                        });
                    }
                    //Log.d(LOG, jsonData);
                }
            });
        }
        else {

            Log.d(LOG, "No Internet Connection");
        }
        //else show no internet connectivity :-(
    }

    private void UpdateAdapter(Poster[] posters) {

        gridAdapter.setPosters(posters);
    }

    private void UpdateDisplay() {

        getPosters("popularity.desc");

    }


    private Poster[] ParsePosters(String jsonString) {

        try {

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            Poster[] posters = new Poster[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject posterObject = jsonArray.getJSONObject(i);

                int id = posterObject.getInt("id");
                String title = posterObject.getString("title");
                String poster_path = posterObject.getString("poster_path");

                posters[i] = new Poster(id, title, poster_path);

                Log.d(LOG, id + "-" + title + "-" + poster_path);
            }
            return posters;
        }catch(JSONException e){

            Log.d(LOG, "Poster Parsing Failed");
            return null;
        }
    }

    private void getMovie(long id) {

        fetchMovie(id);
    }

    private void fetchMovie(long id) {

        String api_key = "199529fbee9c95fdf96c91ea80f2452e";

        final String BASE_URL = "http://api.themoviedb.org/3/movie/";
        long movie_id = id;
        final String API_KEY = "api_key";
        final String APP_TO_RES = "append_to_response";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movie_id + "?")
                .appendQueryParameter(API_KEY, api_key)
                .appendQueryParameter(APP_TO_RES, "trailers")
                .build();


        if (isNetworkAvailable()){

            Log.d(LOG, "Internet Connection Available");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(String.valueOf(uri)).build();

            Log.d(LOG, uri.toString());

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.d(LOG,"Request Failed");

                }

                @Override
                public void onResponse(Response response) throws IOException {

                    String jsonData = response.body().string();

                    if (response.isSuccessful()) {

                        //final Poster[] posters = ParsePosters(jsonData);
                        Movie movie = ParseMovie(jsonData);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                    //Log.d(LOG, jsonData);
                }
            });
        }
        else {

            Log.d(LOG, "No Internet Connection");
        }

    }

    private Movie ParseMovie(String jsonData) {

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            int movieId = jsonObject.getInt("id");
            String title = jsonObject.getString("title");
            String posterPath = jsonObject.getString("poster_path");
            String backDropPath = jsonObject.getString("backdrop_path");
            String overview = jsonObject.getString("overview");
            String releaseDate = jsonObject.getString("release_date");
            String imdbId = jsonObject.getString("imdb_id");
            String homePage = jsonObject.getString("homepage");
            double voteCount = jsonObject.getDouble("vote_count");
            double voteAverage = jsonObject.getDouble("vote_average");
            double budget = jsonObject.getDouble("budget");
            double revenue = jsonObject.getDouble("revenue");

            JSONObject trailerObject = jsonObject.getJSONObject("trailers");
            JSONArray youtubeArray = trailerObject.getJSONArray("youtube");
            Trailer[] trailers = new Trailer[youtubeArray.length()];
            for (int i= 0; i < youtubeArray.length(); i++){

                JSONObject trailerjson = youtubeArray.getJSONObject(i);

                String name = trailerjson.getString("name");
                String source = trailerjson.getString("source");
                String type = trailerjson.getString("type");

                Trailer trailer = new Trailer(name, source, type);
                trailers[i] = trailer;

                Log.d(LOG, name+" "+source+" "+type);
            }

            Movie movie = new Movie(movieId,title,posterPath,backDropPath,overview,releaseDate,
                    trailers,imdbId,homePage,voteCount,voteAverage,budget,revenue);

            Log.d(LOG, movieId+" "+title+" "+posterPath+" "+backDropPath+" "+overview
                    +" "+ releaseDate+" "+" "+imdbId+" "+homePage+" "
                    +voteCount +" "+voteAverage+" "+budget+" "+revenue);

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
        if(networkInfo != null && networkInfo.isConnected()){

            isAvailable = true;
        }

        return isAvailable;
    }

}
