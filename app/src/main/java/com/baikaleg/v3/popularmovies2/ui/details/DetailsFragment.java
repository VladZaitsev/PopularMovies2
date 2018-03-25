package com.baikaleg.v3.popularmovies2.ui.details;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.dagger.scopes.ActivityScoped;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.baikaleg.v3.popularmovies2.data.model.Trailer;
import com.baikaleg.v3.popularmovies2.databinding.FragmentDetailsBinding;
import com.baikaleg.v3.popularmovies2.ui.details.reviews.ReviewPagerAdapter;
import com.baikaleg.v3.popularmovies2.ui.details.trailers.TrailerViewAdapter;
import com.baikaleg.v3.popularmovies2.ui.details.trailers.TrailersItemNavigator;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class DetailsFragment extends DaggerFragment implements TrailersItemNavigator, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final String TAG = DetailsFragment.class.getSimpleName();
    public final static int REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    public final static int REQUEST_READ_EXTERNAL_STORAGE = 102;
    private static final String VIDEO_SOURCE = "YouTube";
    private FragmentDetailsBinding binding;
    private TrailerViewAdapter trailersAdapter;

    private int screenWidth;

    @Inject
    DetailsViewModel viewModel;

    @Inject
    Movie movie;

    @Inject
    public DetailsFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.movie_key))) {
            viewModel.setMovie(savedInstanceState.getParcelable(getString(R.string.movie_key)));
        } else {
            viewModel.setMovie(this.movie);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.onDestroyed();
        trailersAdapter.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        binding.setViewmodel(viewModel);
        binding.setView(this);
        viewModel.setNavigator(new DetailModelNavigator() {
            @Override
            public void onExpandReviewPager(boolean expanded) {
                binding.detailsScrollView.fullScroll(ScrollView.FOCUS_UP);
                binding.detailsScrollView.pageScroll(ScrollView.FOCUS_UP);
                binding.detailsScrollView.setScrollingEnabled(expanded);
            }

            @Override
            public void onExternalStoragePermissionRequest() {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        });

        setHeightAndWidthOfImage();

        ReviewPagerAdapter reviewsAdapter = new ReviewPagerAdapter();
        binding.detailsPagerReviews.setAdapter(reviewsAdapter);
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

        trailersAdapter = new TrailerViewAdapter(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.detailsTrailersContainer.setLayoutManager(layoutManager);
        binding.detailsTrailersContainer.setAdapter(trailersAdapter);
        trailersAdapter.setViewSize(screenWidth / 3, 3 * screenWidth / 12);

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(getString(R.string.movie_key), viewModel.getMovie());
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.setFavorite(!viewModel.getFavorite());
            } else {
                Toast.makeText(getActivity(), R.string.msg_did_not_allow_writing, Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.setFavorite(!viewModel.getFavorite());
            } else {
                Toast.makeText(getActivity(), R.string.msg_did_not_allow_reading, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setHeightAndWidthOfImage() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)
                ? TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                : 0;
        int imageHeight = 0, imageWidth = 0;
        int screenHeight = (getResources().getDisplayMetrics().heightPixels - actionBarHeight);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            imageHeight = screenHeight / 2;
            imageWidth = screenWidth / 2;
        } else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageWidth = screenWidth / 3;
            imageHeight = imageWidth * 4 / 3;
        }
        viewModel.setMainViewHeight(screenHeight - actionBarHeight);

        ViewGroup.LayoutParams params = binding.detailsMainImg.getLayoutParams();
        params.width = imageWidth;
        params.height = imageHeight;
        binding.detailsMainImg.setLayoutParams(params);
    }

    @Override
    public void onTrailerClicked(Trailer trailer) {
        if (trailer.getSite().equals(VIDEO_SOURCE)) {
            Uri uri = Uri.parse(getString(R.string.youtube_base_url) + trailer.getKey());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getActivity().startActivity(intent);
        } else {
            Log.i(TAG, getString(R.string.no_source));
        }
    }
}
