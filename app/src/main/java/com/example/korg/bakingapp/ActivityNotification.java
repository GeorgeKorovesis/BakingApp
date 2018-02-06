package com.example.korg.bakingapp;

/**
 * ---------------------------------------------------------------
 * Created by korg on 3/1/2018.
 * ---------------------------------------------------------------
 * Interface used by adapter to notify main activity about changes
 * ---------------------------------------------------------------
 */

public interface ActivityNotification {
    void notifyActivity(String recipeNameFragment, String recipeIngredientsCard, int recipeId, int stepsId);
    void updateActionBar(String title);
}