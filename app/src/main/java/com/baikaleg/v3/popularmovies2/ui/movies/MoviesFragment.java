package com.baikaleg.v3.popularmovies2.ui.movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.databinding.FragmentMoviesBinding;
import com.baikaleg.v3.popularmovies2.ui.details.DetailsActivity;
import com.baikaleg.v3.popularmovies2.ui.movies.adapter.MovieItemNavigator;
import com.baikaleg.v3.popularmovies2.ui.movies.adapter.MoviesViewAdapter;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Display a grid of {@link Movie}s. User can choose {@link MoviesFilterType#POPULAR_MOVIES},
 * {@link MoviesFilterType#TOP_RATED_MOVIES}, or
 * {@link MoviesFilterType#FAVORITE_MOVIES}
 */
@ActivityScoped
public class MoviesFragment extends DaggerFragment implements MovieItemNavigator {
    private final String KEY_RECYCLER_STATE = "recycler_state";

    private static Bundle bundleRecyclerViewState;

    private MoviesViewAdapter viewAdapter;

    private int rows, columns;

    private FragmentMoviesBinding binding;

    @Inject
    MoviesViewModel moviesViewModel;

    @Inject
    public MoviesFragment() {
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRecyclerViewState = new Bundle();
        Parcelable listState = binding.moviesRv.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleRecyclerViewState != null) {
            Parcelable listState = bundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            viewAdapter.setRecyclerViewState(listState);
        }
        moviesViewModel.start();
    }

    @Override
    public void onDestroy() {
        viewAdapter.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRowsAndColumnsQuantity();

        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        binding.setViewmodel(moviesViewModel);
        binding.setView(this);
        moviesViewModel.setNavigator(() -> viewAdapter.setRecyclerViewState(null));
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), columns);
        binding.moviesRv.setLayoutManager(layoutManager);

        viewAdapter = createAdapter();

        binding.moviesRv.setAdapter(viewAdapter);
        binding.moviesRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            viewAdapter.setRecyclerViewState(listState);
        }
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_popular:
                ((MoviesActivity) getActivity()).setActionBarTitle(getString(R.string.popular));
                moviesViewModel.setFiltering(MoviesFilterType.POPULAR_MOVIES);
                break;
            case R.id.menu_top_rated:
                ((MoviesActivity) getActivity()).setActionBarTitle(getString(R.string.top_rated));
                moviesViewModel.setFiltering(MoviesFilterType.TOP_RATED_MOVIES);
                break;
            case R.id.menu_favorite:
                ((MoviesActivity) getActivity()).setActionBarTitle(getString(R.string.favorite));
                moviesViewModel.setFiltering(MoviesFilterType.FAVORITE_MOVIES);
                break;
        }
        moviesViewModel.loadMovies(true, true);
        return true;
    }

    @NonNull
    private MoviesViewAdapter createAdapter() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
                ? TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                : 0;

        int imageHeight = (getResources().getDisplayMetrics().heightPixels - actionBarHeight) / rows;
        int imageWidth = getResources().getDisplayMetrics().widthPixels / columns;

        MoviesViewAdapter adapter = new MoviesViewAdapter(this, binding.moviesRv);
        adapter.setViewSize(imageWidth, imageHeight);
        return adapter;
    }

    @Override
    public void openMovieDetails(Movie movie) {
        Intent intent = new Intent(getActivity(), DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_RECYCLER_STATE,
                binding.moviesRv.getLayoutManager().onSaveInstanceState());
    }

    private void setRowsAndColumnsQuantity() {
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rows = getResources().getInteger(R.integer.rows_portrait_mode);
            columns = getResources().getInteger(R.integer.columns_portrait_mode);
        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rows = getResources().getInteger(R.integer.rows_landscape_mode);
            columns = getResources().getInteger(R.integer.columns_landscape_mode);
        }
    }
}
