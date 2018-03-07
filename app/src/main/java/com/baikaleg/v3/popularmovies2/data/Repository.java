package com.baikaleg.v3.popularmovies2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.baikaleg.v3.popularmovies2.data.DataSource;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract.MovieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

public class Repository implements DataSource {


    private Context context;

    @Inject
    public Repository(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Movie>> getFavoriteMovies() {
        return makeObservable(() -> {
            List<Movie> movies = new ArrayList<>();
            Cursor cursor = queryMovies();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                movies.add(getMovie(cursor));
                cursor.moveToNext();
            }
            return movies;
        });

    }

    @Override
    public Observable<Movie> getFavoriteMovie() {
        return null;
    }

    @Override
    public void addFavoriteMovie(Movie movie) {
        String stringId = Integer.toString(movie.getId());
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        context.getContentResolver().insert(uri, getContentValues(movie));
    }

    @Override
    public void deleteFavoriteMovie(int id) {
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        context.getContentResolver().delete(uri, null, null);
    }

    private static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(func.call());
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private ContentValues getContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.ID, movie.getId());
        values.put(MovieEntry.TITLE, movie.getTitle());
        return values;
    }

    private Cursor queryMovies() {
        return context.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    private Movie getMovie(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.ID));
        String originalTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE));
        return new Movie(id, originalTitle);
    }
}
