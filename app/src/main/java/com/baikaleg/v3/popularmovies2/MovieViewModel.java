package com.baikaleg.v3.popularmovies2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.MovieDataSource;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Movie;

import javax.inject.Inject;

/**
 * Abstract class for View Models that expose a single {@link Movie}.
 */

public abstract class MovieViewModel extends BaseObservable
        implements MovieDataSource.GetMovieCallback {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> overview = new ObservableField<>();

    public final ObservableField<String> date = new ObservableField<>();

    public final ObservableField<String> url = new ObservableField<>();

    private final ObservableField<Movie> movieObservable = new ObservableField<>();

    private Repository repository;

    public MovieViewModel(@NonNull Repository repository) {
        this.repository = repository;

        movieObservable.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Movie movie = movieObservable.get();
                if (movie != null) {
                    title.set(movie.getTitle());
                    overview.set(movie.getOverview());
                    date.set(movie.getReleaseDate());
                    url.set(movie.getPosterPath());
                }
            }
        });
    }

    @Bindable
    public boolean getFavorite() {
        Movie movie = movieObservable.get();
        return movie.isFavorite();
    }

    public void setFavorite(boolean favorite) {
        Movie movie = movieObservable.get();
        repository.markMovieAsFavorite(movie.getId(), movie.getTitle(), favorite);
    }

    @Override
    public void onDataNotAvailable() {

    }

    @Override
    public void onMovieLoaded(Movie movie) {

    }

    public void setMovie(Movie movie) {
        movieObservable.set(movie);
    }

    public int getMovieId() {
        return movieObservable.get().getId();
    }
}
