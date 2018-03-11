package com.baikaleg.v3.popularmovies2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.Nullable;

import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Movie;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Abstract class for View Models that expose a single {@link Movie}.
 */

public abstract class MovieViewModel extends BaseObservable {

    public final ObservableField<String> title = new ObservableField<>();

    public final ObservableField<String> overview = new ObservableField<>();

    public final ObservableField<String> date = new ObservableField<>();

    public final ObservableField<String> url = new ObservableField<>();

    public final ObservableField<Double> voteAverage = new ObservableField<>();

    public final ObservableField<Boolean> favorite = new ObservableField<>(false);

    private final ObservableField<Movie> movieObservable = new ObservableField<>();

    private Repository repository;

    public MovieViewModel(@Nullable Repository repository) {
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
                    voteAverage.set(movie.getVoteAverage());
                    favorite.set(movie.isFavorite());

                    notifyPropertyChanged(BR.favorite);
                }
            }
        });
    }

    public void start(int movieId) {
        repository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieObservable::set,
                        throwable -> movieObservable.set(null));
    }


    @Bindable
    public boolean getFavorite() {
        return favorite.get();
    }

    public void setFavorite(boolean favorite) {
        this.favorite.set(favorite);

        Movie movie = movieObservable.get();
        repository.markMovieAsFavorite(movie.getId(), movie.getTitle(), movie.getPosterPath(), favorite);

        notifyPropertyChanged(BR.favorite);
    }


    public void setMovie(Movie movie) {
        movieObservable.set(movie);
    }

    protected int getMovieId() {
        return movieObservable.get().getId();
    }
}
