package com.baikaleg.v3.popularmovies2.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.data.network.MovieApi;
import com.baikaleg.v3.popularmovies2.data.network.response.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.ReviewsResponse;
import com.baikaleg.v3.popularmovies2.data.network.response.TrailersResponse;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract;
import com.baikaleg.v3.popularmovies2.data.source.MovieContract.MovieEntry;
import com.baikaleg.v3.popularmovies2.ui.movies.MoviesFilterType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;

public class Repository implements MovieDataSource {
    private static final String VIDEO_TYPE = "Trailer";

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
            return getPopularMovies();
        } else if (type == MoviesFilterType.TOP_RATED_MOVIES) {
            return getTopRatedMovies();
        } else if (type == MoviesFilterType.FAVORITE_MOVIES) {
            return getFavoriteMovies();
        }
        return null;
    }

    @Override
    public Observable<List<Review>> getReviews(int id) {
        return movieApi.createService(context.getString(R.string.base_url))
                .getMovieReviews(id)
                .map(ReviewsResponse::getReviews)
                .toObservable();
    }

    @Override
    public Observable<List<Trailer>> getTrailers(int id) {
        return movieApi.createService(context.getString(R.string.base_url))
                .getTrailers(id)
                .map(TrailersResponse::getTrailers)
                .flatMap(trailers ->
                        Observable.fromIterable(trailers)
                                .filter(trailer -> trailer.getType().equals(VIDEO_TYPE)))
                .take(3)
                .toList()
                .toObservable();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void markMovieAsFavorite(Movie movie, boolean favorite) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        if (favorite) {
            String path = movie.getPosterPath().replace(context.getString(R.string.image_base_url) + "/", "");
            movieApi.createService(context.getString(R.string.image_base_url) + "/")
                    .downloadImage(path)
                    .flatMap(responseBodyResponse -> saveToDisk(responseBodyResponse, path))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        movie.setPosterPath(file.getPath());
                        movie.setFavorite(1);
                        context.getContentResolver().insert(uri, getContentValues(movie));
                    });
        } else {
            String selection = MovieEntry.ID + " = ? ";
            String[] selectionArgs = new String[]{Integer.toString(movie.getId())};
            deleteImage(movie.getPosterPath());
            context.getContentResolver().delete(uri, selection, selectionArgs);
        }
    }

    private Observable<List<Movie>> getPopularMovies() {
        return movieApi.createService(context.getString(R.string.base_url))
                .getPopularMovies()
                .map(MoviesResponse::getMovies)
                .flatMap(movies -> Observable.fromIterable(movies)
                        .doOnNext(movie -> {
                            String path = context.getString(R.string.image_base_url) + movie.getPosterPath();
                            movie.setPosterPath(path);
                            //Find out if movie was marked as favorite
                            String selection = MovieEntry.ID + " = ?";
                            String[] selectionArgs = new String[]{String.valueOf(movie.getId())};
                            try (Cursor cursor = queryMovies(selection, selectionArgs)) {
                                if (cursor.getCount() != 0) {
                                    movie.setFavorite(1);
                                }
                            }
                        })
                        .toList()
                        .toObservable());
    }

    private Observable<List<Movie>> getTopRatedMovies() {
        return movieApi.createService(context.getString(R.string.base_url))
                .getTopRatedMovies()
                .map(MoviesResponse::getMovies)
                .flatMap(movies -> Observable.fromIterable(movies)
                        .doOnNext(movie -> {
                            String path = context.getString(R.string.image_base_url) + movie.getPosterPath();
                            movie.setPosterPath(path);
                            //Find out if movie was marked as favorite
                            String selection = MovieEntry.ID + " = ?";
                            String[] selectionArgs = new String[]{String.valueOf(movie.getId())};
                            try (Cursor cursor = queryMovies(selection, selectionArgs)) {
                                if (cursor.getCount() != 0) {
                                    movie.setFavorite(1);
                                }
                            }
                        })
                        .toList()
                        .toObservable());
    }

    private Observable<List<Movie>> getFavoriteMovies() {
        return makeObservable(() -> {
            List<Movie> movies = new ArrayList<>();
            try (Cursor cursor = queryMovies(null, null)) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.ID));
                    String title = cursor.getString(cursor.getColumnIndex(MovieEntry.TITLE));
                    String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.POSTER_PATH));
                    String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.OVERVIEW));
                    Double rating = cursor.getDouble(cursor.getColumnIndex(MovieEntry.RATING));
                    String release_date = cursor.getString(cursor.getColumnIndex(MovieEntry.RELEASE_DATE));

                    Movie movie = new Movie();
                    movie.setId(id);
                    movie.setTitle(title);
                    movie.setPosterPath(posterPath);
                    movie.setOverview(overview);
                    movie.setReleaseDate(release_date);
                    movie.setVoteAverage(rating);
                    movie.setFavorite(1);

                    movies.add(movie);
                    cursor.moveToNext();
                }
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
        values.put(MovieEntry.POSTER_PATH, movie.getPosterPath());
        values.put(MovieEntry.OVERVIEW, movie.getOverview());
        values.put(MovieEntry.RATING, movie.getVoteAverage());
        values.put(MovieEntry.RELEASE_DATE, movie.getReleaseDate());
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

    private Observable<File> saveToDisk(final Response<ResponseBody> response, String path) {
        return Observable.create(emitter -> {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
                file.mkdirs();
                File destinationFile = new File(file, path);
                destinationFile.createNewFile();
                BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                bufferedSink.writeAll(response.body().source());
                bufferedSink.close();

                emitter.onNext(destinationFile);
                emitter.onComplete();
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        });
    }

    private void deleteImage(String path) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                File file = new File(path);
                file.delete();
                return null;
            }
        }.execute();

    }
}
