package com.example.shakir.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shakir.popularmovies.data.MovieContract.MoviesEntry;

/**
 * Created by Yasin on 8/21/2015.
 */
public class MovieDBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIE_DATABASE = "CREATE TABLE "+ MoviesEntry.TABLE_NAME+"("
                + MoviesEntry._ID +" INTEGER PRIMARY KEY, "
                + MoviesEntry.COLUMN_MOVIE_ID +" INTEGER UNIQUE NOT NULL, "
                + MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_POSTER_PATH + " TEXT, "
                + MoviesEntry.COLUMN_BACKDROP_PATH + " TEXT, "
                + MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_TAGLINE + " TEXT, "
                + MoviesEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + MoviesEntry.COLUMN_RUNTIME + " INTEGER, "
                + MoviesEntry.COLUMN_TRAILERS + " TEXT, "
                + MoviesEntry.COLUMN_IMDB_ID + " TEXT, "
                + MoviesEntry.COLUMN_HOMEPAGE + " TEXT, "
                + MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER, "
                + MoviesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + "UNIQUE (" + MoviesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(CREATE_MOVIE_DATABASE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
