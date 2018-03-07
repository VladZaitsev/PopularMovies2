package com.baikaleg.v3.popularmovies2.ui.movies;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.baikaleg.v3.popularmovies2.BR;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.MoviesResponse;
import com.baikaleg.v3.popularmovies2.data.Repository;
import com.baikaleg.v3.popularmovies2.network.MovieApi;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScoped
public class MoviesViewModel extends BaseObservable {

    public final ObservableList<Movie> items = new ObservableArrayList<>();

    public final ObservableBoolean dataLoading = new ObservableBoolean(false);

    private final Repository repository;

    private MovieApi movieApi;

    private MoviesFilterType currentFilterType = MoviesFilterType.POPULAR_MOVIES;

    private Context context;

    @NonNull
    private final CompositeDisposable compositeDisposable;

    @Inject
    public MoviesViewModel(Context context, Repository repository, MovieApi movieApi) {
        this.repository = repository;
        this.movieApi = movieApi;
        this.context = context;

        compositeDisposable = new CompositeDisposable();
        setFiltering(MoviesFilterType.POPULAR_MOVIES);
    }

    void start() {
        loadMovies(false);
    }

    @Bindable
    public boolean isEmpty() {
        return items.isEmpty();
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

    /**
     * @param showLoadingUI Pass in true to display a loading icon in the UI
     */
    void loadMovies(final boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        Observable<List<Movie>> movieList = Observable.create(emitter -> {
            try {
                List<Movie> list = new ArrayList<>();
                if (currentFilterType == MoviesFilterType.POPULAR_MOVIES) {
                    movieApi.createService()
                            .getPopularMovies()
                            .map(MoviesResponse::getMovies)
                            .subscribe(list::addAll);
                } else if (currentFilterType == MoviesFilterType.TOP_RATED_MOVIES) {
                    movieApi.createService()
                            .getTopRatedMovies()
                            .map(MoviesResponse::getMovies)
                            .subscribe(list::addAll);
                } else if (currentFilterType == MoviesFilterType.FAVORITE_MOVIES) {
                    repository.getFavoriteMovies()
                            .flatMap(movies -> Observable.fromIterable(movies)
                                    .flatMap(movie -> movieApi.createService()
                                            .getMovie(movie.getId()))
                                    .toList()
                                    .toObservable()).subscribe(list::addAll);
                }
                emitter.onNext(list);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
        Disposable disposable = movieList
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(throwable -> {

                })
                .subscribe(movies -> {
                    items.clear();
                    items.addAll(movies);
                    dataLoading.set(false);
                    notifyPropertyChanged(BR.empty);
                }, throwable -> {
                    dataLoading.set(false);
                });
        compositeDisposable.clear();
        compositeDisposable.add(disposable);
    }
}
