package com.example.korg.bakingapp;

import android.telecom.Call;

/**
 * Created by korg on 14/12/2017.
 */

public class RecipeModel {
    int id;
    String name;
    Ingredients [] ingredients;
    Steps [] steps;
    String servings;
    String image;

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Ingredients[] getIngredients(){
        return ingredients;
    }

    public Steps[] getSteps(){
        return steps;
    }

    public String getServings(){
        return servings;
    }

    public String getImage() { return image;}

    public class Ingredients {
        double quantity;
        String measure;
        String ingredient;

        public double getQuantity(){
            return quantity;
        }
        public String getMeasure() {
            return measure;
        }
        public String getIngredient(){
            return ingredient;
        }
    }

    public class Steps {
        int id;
        String shortDescription;
        String description;
        String videoURL;
        String thumbnailURL;

        public int getId(){
            return id;
        }
        public String getShortDescription() {
            return shortDescription;
        }
        public String getDescription(){
            return description;
        }
        public String getVideoURL() {
            return videoURL;
        }
        public String getThumbnailURL(){
            return thumbnailURL;
        }
    }
}