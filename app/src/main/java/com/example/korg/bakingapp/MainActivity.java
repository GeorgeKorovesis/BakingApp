package com.example.korg.bakingapp;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.*;

public class MainActivity extends AppCompatActivity {
    private static boolean fetched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!fetched) {
            RetrofitController controller = new RetrofitController(this);
            controller.start();
            fetched = true;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor c = getContentResolver().query(CONTENT_URI_RECIPES, null, null, null, null);
                System.out.println("@@@@Table RECIPIES@@@@");
                if (c.moveToFirst()) {
                    do {
                        System.out.println(
                                c.getString(c.getColumnIndex(_ID)) +
                                        ", " + c.getInt(c.getColumnIndex(COLUMN_RECIPES_ID)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_RECIPES_NAME)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_RECIPES_IMAGE)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_RECIPES_SERVINGS)));

                    } while (c.moveToNext());
                }

                System.out.println("@@@@Table INGREDIENTS@@@@");
                c = getContentResolver().query(CONTENT_URI_INGREDIENTS, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        System.out.println(
                                c.getString(c.getColumnIndex(_ID)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_INGREDIENTS_INGREDIENT)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_INGREDIENTS_MEASURE)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_INGREDIENTS_QUANTITY)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_INGREDIENTS_RECIPE_ID)));

                    } while (c.moveToNext());
                }

                System.out.println("@@@@Table STEPS@@@@");
                c = getContentResolver().query(CONTENT_URI_STEPS, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        System.out.println(
                                c.getString(c.getColumnIndex(_ID)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_STEPS_DESCRIPTION)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_STEPS_SHORTDESCRIPTION)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_STEPS_THUMBNAILURL)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_STEPS_VIDEOURL)) +
                                        ", " + c.getString(c.getColumnIndex(COLUMN_STEPS_RECIPE_ID)));

                    } while (c.moveToNext());
                }
            }
        }, 5000);
    }
}