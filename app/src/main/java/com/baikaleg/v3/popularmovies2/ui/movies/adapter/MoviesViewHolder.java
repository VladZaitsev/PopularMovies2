package com.baikaleg.v3.popularmovies2.ui.movies.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baikaleg.v3.popularmovies2.databinding.ItemMoviesBinding;

public class MoviesViewHolder extends RecyclerView.ViewHolder {

    public final ItemMoviesBinding binding;

    public MoviesViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}