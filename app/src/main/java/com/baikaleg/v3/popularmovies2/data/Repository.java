package com.baikaleg.v3.popularmovies2.data;

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
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Flowable;
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
    public Flowable<Optional<Movie>> getMovie(int id) {
        return movieApi.createService()
                .getMovie(id)
                .doOnNext(movieOptional -> {
                    if (movieOptional.isPresent()) {
                        Movie movie = movieOptional.get();
                        movie.setFavorite(true);
                    }
                });
    }

    @Override
    public void markMovieAsFavorite(int id, String title, boolean favorite) {
        String stringId = Integer.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        if (favorite) {
            Movie movie = new Movie(id, title);
            context.getContentResolver().insert(uri, getContentValues(movie));
        } else {
            context.getContentResolver().delete(uri, null, null);
        }
    }


    private Observable<List<Movie>> getFavoriteMovies() {
        return makeObservable(() -> {
            List<Movie> movies = new ArrayList<>();
            Cursor cursor = queryMovies();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.ID));
                movieApi.createService()
                        .getMovie(id)
                        .doOnNext(movieOptional -> {
                            if (movieOptional.isPresent()) {
                                Movie movie = movieOptional.get();
                                movies.add(movie);
                            }
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

    private Cursor queryMovies() {
        return context.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }
}
