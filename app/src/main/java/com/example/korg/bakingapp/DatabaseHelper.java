package com.example.korg.bakingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.*;

/**
 * Created by korg on 15/12/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database table
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "recipes.db";

    // Database creation SQL statement
    private static final String TABLE_RECIPES_CREATE = "create table " + TABLE_RECIPES
            + "(" + _ID + " integer primary key autoincrement, "
            + COLUMN_RECIPES_ID + " integer not null, "
            + COLUMN_RECIPES_NAME + " text not null, "
            + COLUMN_RECIPES_SERVINGS + " text not null, "
            + COLUMN_RECIPES_IMAGE + " text not null " + ");";

    private static final String TABLE_INGREDIENTS_CREATE = "create table " + TABLE_INGREDIENTS
            + "(" + _ID + " integer primary key autoincrement, "
            + COLUMN_INGREDIENTS_RECIPE_ID + " integer not null, "
            + COLUMN_INGREDIENTS_QUANTITY + " double not null, "
            + COLUMN_INGREDIENTS_MEASURE + " text not null ,"
            + COLUMN_INGREDIENTS_INGREDIENT + " text not null ,"
            + "FOREIGN KEY (" + COLUMN_INGREDIENTS_RECIPE_ID + ") REFERENCES recipes(" + COLUMN_RECIPES_ID + ")" + ");";

    private static final String TABLE_STEPS_CREATE = "create table " + TABLE_STEPS
            + "(" + _ID + " integer primary key autoincrement, "
            + COLUMN_STEPS_ID + " integer not null, "
            + COLUMN_STEPS_RECIPE_ID + " integer not null, "
            + COLUMN_STEPS_SHORTDESCRIPTION + " text not null ,"
            + COLUMN_STEPS_DESCRIPTION + " text not null ,"
            + COLUMN_STEPS_VIDEOURL + " text not null ,"
            + COLUMN_STEPS_THUMBNAILURL + " text ,"
            + "FOREIGN KEY (" + COLUMN_STEPS_RECIPE_ID + ") REFERENCES recipes(" + COLUMN_RECIPES_ID + ")" + ");";


    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_RECIPES_CREATE);
        db.execSQL(TABLE_INGREDIENTS_CREATE);
        db.execSQL(TABLE_STEPS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ".concat(TABLE_RECIPES));
        db.execSQL("DROP TABLE IF EXISTS ".concat(TABLE_INGREDIENTS));
        db.execSQL("DROP TABLE IF EXISTS ".concat(TABLE_STEPS));
        onCreate(db);
    }
}
