package com.example.shakir.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shakir.popularmovies.R;
import com.example.shakir.popularmovies.data.MovieContract;
import com.example.shakir.popularmovies.model.Poster;
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

        final ViewHolder viewHolder;

        if(convertView == null){

            convertView = LayoutInflater.from(mContext).inflate(R.layout.poster_item_view,null);

            viewHolder = new ViewHolder();
            viewHolder.posterView = (ImageView) convertView.findViewById(R.id.posterView);
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            viewHolder.fav = (ImageView) convertView.findViewById(R.id.likeUnchecked);

            convertView.setTag(viewHolder);
        }
        else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        String url = mPosters[position].getFinalPosterPath();
        Log.d(LOG, url);

        Picasso.with(mContext)
                .load(url)
                .fit()
                .placeholder(R.drawable.poster_placeholder)
                .into(viewHolder.posterView);


        String title = mPosters[position].getTitle();
        if (title.length() > 11){

            viewHolder.titleView.setText(title.substring(0,9)+"...");
        }
        else {

            viewHolder.titleView.setText(title);
        }

        if (isFavourite(mPosters[position].getMovieId(),mContext)){

            viewHolder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);

        }

        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    public static class ViewHolder{

        ImageView posterView;
        TextView titleView;
        ImageView fav;
    }

    public static boolean isFavourite(long movie_id,Context context){
        boolean favourite = false;

        Cursor movieCursor = context.getContentResolver().query(
                MovieContract.MoviesEntry.CONTENT_URI,
                new String[]{MovieContract.MoviesEntry.COLUMN_MOVIE_ID},
                MovieContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movie_id)},
                null
        );

        if(movieCursor.moveToFirst()){
            favourite  = true;
        }

        movieCursor.close();
        return favourite;
    }
}
