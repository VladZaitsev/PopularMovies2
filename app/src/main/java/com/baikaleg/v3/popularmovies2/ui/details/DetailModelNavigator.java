package com.baikaleg.v3.popularmovies2.ui.details;

/**
 * Defines the navigation actions that can be called from a list item in the movie list.
 */
public interface DetailModelNavigator {

    void onExpandReviewPager(boolean expanded);

    void onExternalStoragePermissionRequest();
}
