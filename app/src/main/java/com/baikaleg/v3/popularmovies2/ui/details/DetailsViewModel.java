package com.baikaleg.v3.popularmovies2.ui.details;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.ui.details.adapter.ReviewItemNavigator;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends MovieViewModel {

    private ReviewItemNavigator navigator;

    public final ObservableList<Review> items = new ObservableArrayList<>();

    public final ObservableBoolean pagerExpanded = new ObservableBoolean(false);

    public final ObservableField<Integer> currentPagerPage = new ObservableField<>();

    private int imageHeight, imageWidth, mainViewHeight;

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
                            if (reviews.size() == 0) {
                                currentPagerPage.set(0);
                            } else {
                                currentPagerPage.set(1);
                            }
                            notifyChange();
                        },
                        throwable -> Log.i("eee", throwable.getMessage()));

    }

    public void favoriteBtnClicked() {
        setFavorite(!getFavorite());
    }

    public void expandBtnClicked() {
        boolean temp = !pagerExpanded.get();
        pagerExpanded.set(temp);
        navigator.onExpandReviewPager(!temp);
    }

    public void setNavigator(@NonNull ReviewItemNavigator navigator) {
        this.navigator = navigator;
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
    public int getImageHeight() {
        return imageHeight;
    }

    void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Bindable
    public float getMainViewHeight() {
        return mainViewHeight;
    }

    void setMainViewHeight(int mainViewHeight) {
        this.mainViewHeight = mainViewHeight;
    }

    @Bindable
    public int getImageWidth() {
        return imageWidth;
    }

    void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;

    }

    void setCurrentPagerPage(int page) {
        currentPagerPage.set(page);
    }
}
