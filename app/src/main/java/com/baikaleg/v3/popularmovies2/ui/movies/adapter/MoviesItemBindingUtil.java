package com.baikaleg.v3.popularmovies2.ui.movies.adapter;

import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.baikaleg.v3.popularmovies2.R;
import com.baikaleg.v3.popularmovies2.data.model.Movie;
import com.squareup.picasso.Picasso;

/**
 * Contains {@link BindingAdapter}s for the {@link Movie} item.
 */
public class MoviesItemBindingUtil {

    @BindingAdapter({"image", "height", "width"})
    public static void setImageUrl(@NonNull ImageView imageView, @NonNull String url, int height, int width) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Picasso.with(imageView.getContext())
                .load(imageView.getContext().getString(R.string.image_base_url) + url)
                .resize(width, height)
                .into(imageView);
    }
}
