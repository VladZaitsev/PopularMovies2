package com.baikaleg.v3.popularmovies2.ui.details;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.databinding.FragmentDetailsBinding;
import com.baikaleg.v3.popularmovies2.ui.details.adapter.ReviewPagerAdapter;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class DetailsFragment extends DaggerFragment {
    private FragmentDetailsBinding binding;

    @Inject
    int movieId;

    @Inject
    DetailsViewModel viewModel;

    @Inject
    public DetailsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start(movieId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroyed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHeightAndWidthOfImage();

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);
        binding.setView(this);
        viewModel.setNavigator(expanded -> {
            binding.detailsScrollView.fullScroll(ScrollView.FOCUS_UP);
            binding.detailsScrollView.pageScroll(ScrollView.FOCUS_UP);
            binding.detailsScrollView.setScrollingEnabled(expanded);
        });

        ReviewPagerAdapter adapter = new ReviewPagerAdapter();
        binding.detailsPagerReviews.setAdapter(adapter);
        binding.detailsPagerReviews.setCurrentItem(0);

        binding.detailsPagerReviews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.setCurrentPagerPage(position + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return binding.getRoot();
    }

    private void setHeightAndWidthOfImage() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
                ? TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                : 0;
        int imageHeight = 0, imageWidth = 0, mainViewHeight;
        mainViewHeight = (getResources().getDisplayMetrics().heightPixels - actionBarHeight);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageHeight = mainViewHeight / 2;
            imageWidth = getResources().getDisplayMetrics().widthPixels / 2;
        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageHeight = mainViewHeight;
            imageWidth = imageHeight * 3 / 4;
        }
        viewModel.setMainViewHeight(mainViewHeight - actionBarHeight);
        viewModel.setImageWidth(imageWidth);
        viewModel.setImageHeight(imageHeight);
    }
}
