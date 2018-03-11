package com.baikaleg.v3.popularmovies2.ui.details;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.databinding.FragmentDetailsBinding;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class DetailsActivityFragment extends DaggerFragment {

    @Inject
    int movieId;

    @Inject
    DetailsViewModel viewModel;

    @Inject
    public DetailsActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start(movieId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHeightAndWidthOfImage();

        FragmentDetailsBinding binding = FragmentDetailsBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);
        binding.setView(this);

        return binding.getRoot();
    }

    private void setHeightAndWidthOfImage() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
                ? TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                : 0;
        int height = 0, width = 0;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            height = (getResources().getDisplayMetrics().heightPixels - actionBarHeight) / 2;
            width = getResources().getDisplayMetrics().widthPixels / 2;
        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = (getResources().getDisplayMetrics().heightPixels - actionBarHeight);
            width = height * 3 / 4;
        }
        viewModel.setWidth(width);
        viewModel.setHeight(height);
    }
}
