package com.baikaleg.v3.popularmovies2.ui.details;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.MovieViewModel;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Review;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class DetailsViewModel extends MovieViewModel {
    private static final String TAG = DetailsViewModel.class.getSimpleName();

    private DetailModelNavigator navigator;

    public final ObservableList<Review> reviewsList = new ObservableArrayList<>();

    public final ObservableList<Trailer> trailersList = new ObservableArrayList<>();

    public final ObservableBoolean pagerExpanded = new ObservableBoolean(false);

    public final ObservableField<Integer> currentPagerPage = new ObservableField<>(0);

    private int mainViewHeight;

    private final Repository repository;

    @Inject
    public DetailsViewModel(Repository repository) {
        super();
        this.repository = repository;
    }

    void onDestroyed() {
        // Clear references to avoid potential memory leaks.
        callback = null;
    }

    void start() {
        repository.getReviews(movieObservable.get().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reviews -> {
                    reviewsList.clear();
                    reviewsList.addAll(reviews);
                    if (reviews.size() == 0) {
                        currentPagerPage.set(0);
                    } else {
                        currentPagerPage.set(1);
                    }
                    notifyChange();
                }, throwable -> Log.i(TAG, throwable.getMessage()));

        repository.getTrailers(movieObservable.get().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(trailers -> {
                    trailersList.clear();
                    trailersList.addAll(trailers);
                    notifyChange();
                }, throwable -> Log.i(TAG, throwable.getMessage()));
    }

    public void favoriteBtnClicked() {
        setFavorite(!getFavorite());
    }

    public void expandBtnClicked() {
        boolean temp = !pagerExpanded.get();
        pagerExpanded.set(temp);
        navigator.onExpandReviewPager(!temp);
    }

    void setNavigator(@NonNull DetailModelNavigator navigator) {
        this.navigator = navigator;
    }

    @Bindable
    public boolean getFavorite() {
        return favorite.get();
    }

    public void setFavorite(boolean favorite) {
        this.favorite.set(favorite);
        notifyPropertyChanged(BR.favorite);

        movieObservable.get().setFavorite(favorite ? 1 : 0);
        repository.markMovieAsFavorite(movieObservable.get());
    }

    @Bindable
    public float getMainViewHeight() {
        return mainViewHeight;
    }

    void setMainViewHeight(int mainViewHeight) {
        this.mainViewHeight = mainViewHeight;
    }

    void setCurrentPagerPage(int page) {
        currentPagerPage.set(page);
    }
}
