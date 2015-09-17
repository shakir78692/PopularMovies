package com.example.shakir.popularmovies.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shakir.popularmovies.model.Poster;
import com.example.shakir.popularmovies.R;
import com.squareup.picasso.Picasso;

public class GridAdapter extends BaseAdapter {

    private static final String LOG = GridAdapter.class.getSimpleName();

    Context mContext;
    Poster[] mPosters;

    public GridAdapter(Context context, Poster[] posters){

        mContext = context;
        mPosters = posters;
    }

    public void setPosters(Poster[] posters){
        mPosters = posters;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mPosters.length;
    }

    @Override
    public Object getItem(int position) {
        return mPosters[position];
    }

    @Override
    public long getItemId(int position) {
        return mPosters[position].getMovieId();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        if(convertView == null){
            imageView = new ImageView(mContext);
            convertView = imageView;
        }
        else{

            imageView = (ImageView) convertView;
        }

        String url = mPosters[position].getFinalPosterPath();
        Log.d(LOG, url);

        Picasso.with(mContext)
                .load(url)
                .fit().centerInside()
                .placeholder(R.drawable.xyz)
                .into(imageView);

        return convertView;
    }
}
