package com.baikaleg.v3.popularmovies2;

import android.databinding.BaseObservable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.baikaleg.v3.popularmovies2.data.model.Movie;

/**
 * Abstract class for View Models that expose a single {@link Movie}.
 */

public abstract class MovieViewModel extends BaseObservable {

    public final ObservableBoolean dataLoading = new ObservableBoolean(true);

    public final ObservableField<Movie> movieObservable = new ObservableField<>();

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> overview = new ObservableField<>();

    public final ObservableField<String> date = new ObservableField<>();

    public final ObservableField<String> url = new ObservableField<>();

    public final ObservableField<Double> voteAverage = new ObservableField<>();

    public final ObservableField<Boolean> favorite = new ObservableField<>(false);


    public MovieViewModel() {
        movieObservable.addOnPropertyChangedCallback(callback);
    }

    public void setMovie(Movie movie) {
        movieObservable.set(movie);
    }

    protected OnPropertyChangedCallback callback = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            Movie movie = movieObservable.get();
            if (movie != null) {
                title.set(movie.getTitle());
                overview.set(movie.getOverview());
                date.set(movie.getReleaseDate());
                url.set(movie.getPosterPath());
                voteAverage.set(movie.getVoteAverage());
                favorite.set(movie.isFavorite());

                notifyChange();
            }
        }
    };
}
