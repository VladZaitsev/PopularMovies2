package com.baikaleg.v3.popularmovies2.ui.movies;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.data.model.Movie;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class MoviesViewModel extends BaseObservable {

    public final ObservableList<Movie> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    public final ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    private final Repository repository;

    private MoviesFilterType currentFilterType = MoviesFilterType.POPULAR_MOVIES;

    @NonNull
    private final CompositeDisposable compositeDisposable;

    @Inject
    public MoviesViewModel(Repository repository) {
        this.repository = repository;

        compositeDisposable = new CompositeDisposable();
        setFiltering(MoviesFilterType.POPULAR_MOVIES);
    }

    void start() {
        loadMovies(false);
    }

    @Bindable
    public boolean isEmpty() {
        return !isDataLoadingError.get() && items.isEmpty();
    }

    void onDestroyed() {
        // Clear references to avoid potential memory leaks.
        compositeDisposable.clear();
    }

    /**
     * Sets the current movie filtering type
     *
     * @param type Can be {@link MoviesFilterType#POPULAR_MOVIES},
     *             {@link MoviesFilterType#TOP_RATED_MOVIES}, or
     *             {@link MoviesFilterType#FAVORITE_MOVIES}
     */
    void setFiltering(MoviesFilterType type) {
        currentFilterType = type;
    }

    MoviesFilterType getFiltering() {
        return currentFilterType;
    }

    public void loadMovies(final boolean forceUpdate) {
        loadMovies(forceUpdate, true);
    }

    private void loadMovies(final boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }

        if (forceUpdate) {
            repository.refreshMovies();
        }

        compositeDisposable.clear();
        Disposable disposable = repository.getMovies(currentFilterType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    if (showLoadingUI) {
                        dataLoading.set(false);
                    }
                    isDataLoadingError.set(false);
                    items.clear();
                    items.addAll(movies);
                    notifyPropertyChanged(BR.empty);
                }, throwable -> showError());

        compositeDisposable.add(disposable);
    }

    private void showError() {
        dataLoading.set(false);
        isDataLoadingError.set(true);
        notifyPropertyChanged(BR.empty);
    }
}
