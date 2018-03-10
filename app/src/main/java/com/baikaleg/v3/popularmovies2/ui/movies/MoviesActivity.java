package com.baikaleg.v3.popularmovies2.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.ui.details.DetailsActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MoviesActivity extends DaggerAppCompatActivity implements MovieItemNavigator {

    @Inject
    MoviesViewModel viewModel;

    @Inject
    MoviesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MoviesFragment moviesFragment =
                (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if (moviesFragment == null) {
            moviesFragment = fragment;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, moviesFragment);
            transaction.commit();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.type_movies_key))) {
            MoviesFilterType currentType = (MoviesFilterType) savedInstanceState.get(getString(R.string.type_movies_key));
            if (currentType != null) {
                viewModel.setFiltering(currentType);
            }
        }
        titleSetting();
    }

    @Override
    protected void onDestroy() {
        viewModel.onDestroyed();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(getString(R.string.type_movies_key), viewModel.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void openMovieDetails(int id) {
        Intent intent=new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID,id);
        startActivity(intent);
    }

    @SuppressWarnings("ConstantConditions")
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void titleSetting() {
        if (viewModel.getFiltering() == MoviesFilterType.POPULAR_MOVIES) {
            setTitle(getString(R.string.popular));
        } else if (viewModel.getFiltering() == MoviesFilterType.TOP_RATED_MOVIES) {
            setTitle(getString(R.string.top_rated));
        } else if (viewModel.getFiltering() == MoviesFilterType.FAVORITE_MOVIES) {
            setTitle(getString(R.string.favorite));
        }
    }
}
