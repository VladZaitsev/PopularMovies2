package com.baikaleg.v3.popularmovies2.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract.MovieEntry;
import com.baikaleg.v3.popularmovies2.network.MovieApi;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFilterType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;

public class Repository implements MovieDataSource {

    private Context context;
    private MovieApi movieApi;

    @Inject
    public Repository(Context context, MovieApi movieApi) {
        this.context = context;
        this.movieApi = movieApi;
    }

    @Override
    public Observable<List<Movie>> getMovies(MoviesFilterType type) {
        if (type == MoviesFilterType.POPULAR_MOVIES) {
            return movieApi.createService()
                    .getPopularMovies()
                    .map(MoviesResponse::getMovies)
                    .toObservable();
        } else if (type == MoviesFilterType.TOP_RATED_MOVIES) {
            return movieApi.createService()
                    .getTopRatedMovies()
                    .map(MoviesResponse::getMovies)
                    .toObservable();
        } else if (type == MoviesFilterType.FAVORITE_MOVIES) {
            return getFavoriteMovies();
        }
        return null;
    }

    @Override
    public Observable<Movie> getMovie(int id) {
        return movieApi.createService().getMovie(id).map(movie -> {
            String selection = MovieEntry.ID + " = ? ";
            String[] selectionArgs = new String[]{Integer.toString(id)};
            try (Cursor cursor = queryMovies(selection, selectionArgs)) {
                if (cursor.getCount() != 0) {
                    movie.setFavorite(true);
                }
                return movie;
            }
        });
    }

    @Override
    public void markMovieAsFavorite(int id, String title, boolean favorite) {
       Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        //  Uri uri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, 3);
        if (favorite) {
            Movie movie = new Movie(id, title);
            context.getContentResolver().insert(uri, getContentValues(movie));
        } else {
            String selection = MovieEntry.ID + " = ? ";
            String[] selectionArgs = new String[]{Integer.toString(id)};
            context.getContentResolver().delete(uri, selection, selectionArgs);
        }
    }

    private Observable<List<Movie>> getFavoriteMovies() {
        return makeObservable(() -> {
            List<Movie> movies = new ArrayList<>();
            Cursor cursor = queryMovies(null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.ID));
                movieApi.createService()
                        .getMovie(id)
                        .doOnNext(movie -> {
                            movies.add(movie);
                        });
                cursor.moveToNext();
            }
            return movies;
        });
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

    private Cursor queryMovies(String selection, String[] selectionArgs) {
        return context.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null
        );
    }
}
