package com.baikaleg.v3.popularmovies2.data.source;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    private MovieContract() {
    }

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.baikaleg.v3.popularmovies2";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES= "movies";

    public static abstract class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";

        public static final String ID = "id";

        public static final String TITLE = "title";

        public static final String POSTER_PATH = "posterPath";

        public static final String RELEASE_DATE = "releaseDate";

        public static final String OVERVIEW = "overview";

        public static final String RATING = "rating";
    }
}