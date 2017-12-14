package com.example.korg.bakingapp;

import android.telecom.Call;

/**
 * Created by korg on 14/12/2017.
 */

public class BakingModel {
    int id;
    String name;
    Recipe [] recipe;

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Recipe[] getRecipe(){
        return recipe;
    }

    class Recipe {
        int quantity;
        String measure;
        String ingredient;

        public int getQuantity(){
            return quantity;
        }
        public String getMeasure() {
            return measure;
        }
        public String getIngredient(){
            return ingredient;
        }
    }
}