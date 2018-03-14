package com.baikaleg.v3.popularmovies2.ui.details;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends MovieViewModel {

    public final ObservableList<Review> items = new ObservableArrayList<>();

    private int height, width;

    private Repository repository;

    @Inject
    public DetailsViewModel(Repository repository) {
        super();
        this.repository = repository;
    }

    void onDestroyed() {
        // Clear references to avoid potential memory leaks.
        callback = null;
    }

    void start(int movieId) {
        repository.getMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieObservable::set,
                        throwable -> movieObservable.set(null));

        repository.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> {
                            items.clear();
                            items.addAll(reviews);
                            notifyChange();
                        },
                        throwable -> Log.i("eee", throwable.getMessage()));

    }

    public void favoriteClicked() {
        setFavorite(!getFavorite());
    }

    @Bindable
    public boolean getFavorite() {
        return favorite.get();
    }

    public void setFavorite(boolean favorite) {
        this.favorite.set(favorite);

        Movie movie = movieObservable.get();
        repository.markMovieAsFavorite(movie, favorite);

        notifyPropertyChanged(BR.favorite);
    }

    @Bindable
    public int getHeight() {
        return height;
    }

    @Bindable
    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;

    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
