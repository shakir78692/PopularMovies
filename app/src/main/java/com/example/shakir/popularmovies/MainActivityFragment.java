package com.example.shakir.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

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

    protected Poster[] mPosters;
    protected GridAdapter gridAdapter;
    protected GridView gridView;

    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected SwipeRefreshLayout.OnRefreshListener mRefreshListner = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            UpdateDisplay();
        }
    };

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

        TextView textView = (TextView) rootView.findViewById(android.R.id.empty);
        mPosters = new Poster[0];
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setEmptyView(textView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListner);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue_500, R.color.red_500,
                R.color.yellow_500, R.color.green_500);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        UpdateDisplay();
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {


                    String jsonData = response.body().string();

                    if (response.isSuccessful()) {

                        final Poster[] posters = ParsePosters(jsonData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                mPosters = posters;
                                UpdateAdapter();
                            }
                        });
                    }
                }
            });
        }
        else {

            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }

            Log.d(LOG, "No Internet Connection");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.general_error_title)
                    .setMessage(R.string.no_internet_error)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void UpdateAdapter() {

        gridAdapter = new GridAdapter(getActivity(),mPosters);
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("MOVIE_ID", id);
                startActivity(intent);
            }
        });

    }

    private void UpdateDisplay() {

        SharedPreferences sharedprfs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = sharedprfs.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_popularity));
        getPosters(sort_by);

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
