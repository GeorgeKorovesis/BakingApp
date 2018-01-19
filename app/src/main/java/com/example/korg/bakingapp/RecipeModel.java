package com.example.korg.bakingapp;

/**
 * Created by korg on 14/12/2017.
 */

public class RecipeModel {
    int id;
    String name;
    Ingredients[] ingredients;
    Steps[] steps;
    String servings;
    String image;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    Ingredients[] getIngredients() {
        return ingredients;
    }

    Steps[] getSteps() {
        return steps;
    }

    String getServings() {
        return servings;
    }

    String getImage() {
        return image;
    }

    class Ingredients {
        double quantity;
        String measure;
        String ingredient;

        double getQuantity() {
            return quantity;
        }

        String getMeasure() {
            return measure;
        }

        String getIngredient() {
            return ingredient;
        }
    }

    class Steps {
        int id;
        String shortDescription;
        String description;
        String videoURL;
        String thumbnailURL;

        int getId() {
            return id;
        }

        String getShortDescription() {
            return shortDescription;
        }

        String getDescription() {
            return description;
        }

        String getVideoURL() {
            return videoURL;
        }

        String getThumbnailURL() {
            return thumbnailURL;
        }
    }
}