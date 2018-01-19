package com.example.korg.bakingapp;

/**
 * Created by korg on 3/1/2018.
 */

public interface ActivityNotification {
    void notifyActivity(String recipeNameFragment, String recipeIngredientsCard, int recipeId, int stepsId);

    void updateActionBar(String title);
}
