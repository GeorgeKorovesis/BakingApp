package com.example.korg.bakingapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by korg on 19/12/2017.
 */

public class BakingContract {

    public static final String AUTHORITY = "com.example.korg.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_STEPS = "steps";
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final class BakingEntry implements BaseColumns{

        public static final Uri CONTENT_URI_RECIPES =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_RECIPES = "recipes";
        public static final String COLUMN_RECIPES_ID = "id";
        public static final String COLUMN_RECIPES_SERVINGS = "servings";
        public static final String COLUMN_RECIPES_IMAGE = "image";
        public static final String COLUMN_RECIPES_NAME = "name";


        public static final Uri CONTENT_URI_INGREDIENTS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_INGREDIENTS = "ingredients";
        public static final String COLUMN_INGREDIENTS_RECIPE_ID = "recipe_id";
        public static final String COLUMN_INGREDIENTS_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENTS_MEASURE = "measure";
        public static final String COLUMN_INGREDIENTS_INGREDIENT = "ingredient";


        public static final Uri CONTENT_URI_STEPS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_STEPS = "steps";
        public static final String COLUMN_STEPS_ID = "id";
        public static final String COLUMN_STEPS_RECIPE_ID = "recipe_id";
        public static final String COLUMN_STEPS_SHORTDESCRIPTION = "short_description";
        public static final String COLUMN_STEPS_DESCRIPTION = "description";
        public static final String COLUMN_STEPS_VIDEOURL = "video_url";
        public static final String COLUMN_STEPS_THUMBNAILURL = "thumbnail_url";

    }


}
