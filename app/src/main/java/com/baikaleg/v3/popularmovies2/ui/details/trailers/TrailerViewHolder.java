package com.baikaleg.v3.popularmovies2.ui.details.trailers;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baikaleg.v3.popularmovies2.databinding.ItemTrailerBinding;

public class TrailerViewHolder extends RecyclerView.ViewHolder {

    public final ItemTrailerBinding binding;

    public TrailerViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}