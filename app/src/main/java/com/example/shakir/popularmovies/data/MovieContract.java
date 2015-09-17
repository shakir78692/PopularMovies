package com.example.shakir.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract{

    public static final String CONTENT_AUTHORITY = "com.example.shakir.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static class MoviesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String MOVIES_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String MOVIE_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_TAGLINE = "tagline";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_RUNTIME = "runtime";

        public static final String COLUMN_TRAILERS = "trailers";

        public static final String COLUMN_IMDB_ID = "imdb_id";

        public static final String COLUMN_HOMEPAGE = "homepage";

        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static Uri buildMovieUri(long id){

            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }
}
