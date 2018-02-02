package com.example.korg.bakingapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by korg on 19/12/2017.
 */

public class BakingContract {

    static final String AUTHORITY = "com.example.korg.bakingapp";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    static final String PATH_RECIPES = "recipe_name";
    static final String PATH_STEPS = "steps";
    static final String PATH_INGREDIENTS = "ingredients";

    public static final class BakingEntry implements BaseColumns {

        static final Uri CONTENT_URI_RECIPES =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        static final String TABLE_RECIPES = "recipe_name";
        static final String COLUMN_RECIPES_ID = "id";
        static final String COLUMN_RECIPES_SERVINGS = "servings";
        static final String COLUMN_RECIPES_IMAGE = "image";
        static final String COLUMN_RECIPES_NAME = "name";


        static final Uri CONTENT_URI_INGREDIENTS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        static final String TABLE_INGREDIENTS = "ingredients";
        static final String COLUMN_INGREDIENTS_RECIPE_ID = "recipe_id";
        static final String COLUMN_INGREDIENTS_QUANTITY = "quantity";
        static final String COLUMN_INGREDIENTS_MEASURE = "measure";
        static final String COLUMN_INGREDIENTS_INGREDIENT = "ingredient";


        static final Uri CONTENT_URI_STEPS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        static final String TABLE_STEPS = "steps";
        static final String COLUMN_STEPS_ID = "id";
        static final String COLUMN_STEPS_RECIPE_ID = "recipe_id";
        static final String COLUMN_STEPS_SHORTDESCRIPTION = "short_description";
        static final String COLUMN_STEPS_DESCRIPTION = "description";
        static final String COLUMN_STEPS_VIDEOURL = "video_url";
        static final String COLUMN_STEPS_THUMBNAILURL = "thumbnail_url";

    }
}
