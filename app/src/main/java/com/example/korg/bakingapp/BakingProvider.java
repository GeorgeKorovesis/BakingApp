package com.example.korg.bakingapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashMap;

import static com.example.korg.bakingapp.BakingContract.*;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.*;

/**
 * Created by korg on 18/12/2017.
 */

public class BakingProvider extends ContentProvider {

    static final int RECIPES = 1;
    static final int RECIPE_ID = 2;
    static final int INGREDIENTS = 3;
    static final int INGREDIENTS_ID = 4;
    static final int STEPS = 5;
    static final int STEPS_ID = 6;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_RECIPES, RECIPES);
        uriMatcher.addURI(AUTHORITY, PATH_RECIPES + "/#", RECIPE_ID);
        uriMatcher.addURI(AUTHORITY, PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(AUTHORITY, PATH_INGREDIENTS + "/#", INGREDIENTS_ID);
        uriMatcher.addURI(AUTHORITY, PATH_STEPS, STEPS);
        uriMatcher.addURI(AUTHORITY, PATH_STEPS + "/#", STEPS_ID);
    }

    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        db = dbHelper.getReadableDatabase();
        int uriMatch = uriMatcher.match(uri);
        Cursor cursor;

        switch (uriMatch) {
            case RECIPES:
                cursor = db.query(TABLE_RECIPES,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case INGREDIENTS:
                cursor = db.query(TABLE_INGREDIENTS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case STEPS:
                cursor = db.query(TABLE_STEPS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri returnUri;
        db = dbHelper.getWritableDatabase();
        long id;
        switch (uriMatcher.match(uri)) {
            case RECIPES:
                id = db.insert(TABLE_RECIPES, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            case INGREDIENTS:
                id = db.insert(TABLE_INGREDIENTS, null, values);

                if (id > 0)
                    returnUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            case STEPS:

                id = db.insert(TABLE_STEPS, null, values);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(uri, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
