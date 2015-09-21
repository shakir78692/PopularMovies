package com.example.shakir.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shakir.popularmovies.R;
import com.example.shakir.popularmovies.fragment.FavouriteFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Shakir on 21/09/15.
 */
public class FavouriteAdapter extends CursorAdapter {


    private static final int VIEW_TYPE_COUNT = 1;

    public static class ViewHolder{

        public final ImageView posterView;
        public final TextView titleView;
        public final ImageView fav;

        public ViewHolder(View view){
            posterView = (ImageView) view.findViewById(R.id.posterView);
            titleView = (TextView) view.findViewById(R.id.titleView);
            fav = (ImageView) view.findViewById(R.id.likeUnchecked);
        }
    }

    public FavouriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.poster_item_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String title = cursor.getString(FavouriteFragment.COL_TITLE);
        if (title.length() > 11){

            viewHolder.titleView.setText(title.substring(0,9)+"...");
        }
        else {

            viewHolder.titleView.setText(title);
        }

        String url = "http://image.tmdb.org/t/p/w185/" + cursor.getString(FavouriteFragment.COL_POSTER);

        Picasso.with(context)
                .load(url)
                .fit()
                .placeholder(R.drawable.poster_placeholder)
                .into(viewHolder.posterView);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_COUNT;
    }
}
