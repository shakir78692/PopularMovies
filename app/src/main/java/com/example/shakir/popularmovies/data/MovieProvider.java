package com.example.shakir.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import static com.example.shakir.popularmovies.data.MovieContract.*;


public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIE_ID = 101;

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE+"/#",MOVIE_ID);

        return matcher;
    }


    private static final SQLiteQueryBuilder mQueryBuilder;
    static {

        mQueryBuilder = new SQLiteQueryBuilder();
        mQueryBuilder.setTables(MoviesEntry.TABLE_NAME);

    }


    private static final String sMovieSelection = MoviesEntry.TABLE_NAME+"."
            + MoviesEntry.COLUMN_MOVIE_ID+" = ? ";


    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder){

        String movie_id =  MoviesEntry.getMovieIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sMovieSelection;
        selectionArgs = new String[]{movie_id};

        return mQueryBuilder.query(mOpenHelper.getReadableDatabase(),projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)){

            case MOVIE_ID:
                retCursor = getMovieById(uri, projection, sortOrder);
                break;

            case MOVIES:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIES:
                return MoviesEntry.MOVIES_TYPE;
            case MOVIE_ID:
                return MoviesEntry.MOVIE_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIES:
                long _id = db.insert(MoviesEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = MoviesEntry.buildMovieUri(_id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";
        switch (match){
            case MOVIES:
                rowsDeleted = db.delete(MoviesEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case MOVIE_ID:
                String movie_id = MoviesEntry.getMovieIdFromUri(uri);
                rowsDeleted = db.delete(MoviesEntry.TABLE_NAME,
                        MoviesEntry.COLUMN_MOVIE_ID+" + "+movie_id+
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection +')': ""),selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        if (null == selection) selection = "1";
        switch (match){
            case MOVIES:
                rowsUpdated = db.update(MoviesEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case MOVIE_ID:
                String movie_id = MoviesEntry.getMovieIdFromUri(uri);
                rowsUpdated = db.update(MoviesEntry.TABLE_NAME,
                        values,
                        MoviesEntry.COLUMN_MOVIE_ID+" + "+ movie_id +
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection +')': ""),
                        selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesEntry.TABLE_NAME, null, value);
                        if (_id > 0) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
