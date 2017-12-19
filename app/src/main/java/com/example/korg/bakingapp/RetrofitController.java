package com.example.korg.bakingapp;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.*;
import com.example.korg.bakingapp.RecipeModel.*;
/**
 * Created by korg on 14/12/2017.
 */

public class RetrofitController implements Callback<RecipeModel[]> {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static Context context;

    public RetrofitController(Context context){
        this.context = context;
    }

    public void start() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitInterface serviceAPI = retrofit.create(RetrofitInterface.class);

        Call<RecipeModel[]> call = serviceAPI.getData();
        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<RecipeModel[]> call, Response<RecipeModel[]> response) {
        if (response.isSuccessful()) {

            RecipeModel[] recipes = response.body();
            Ingredients[] ingredients;
            Steps[] steps;

            ContentValues cv = new ContentValues();

            int recipe_id=0;

            if(recipes == null)
                return;

            for(RecipeModel recipe: recipes) {
                System.out.println("@@@@recipe.getId()="+recipe.getId());
                cv.clear();
                cv.put(COLUMN_RECIPES_NAME, recipe.getName());
                cv.put(COLUMN_RECIPES_SERVINGS, recipe.getServings());
                cv.put(COLUMN_RECIPES_IMAGE, recipe.getImage());
                cv.put(COLUMN_RECIPES_ID, recipe.getId());
                context.getContentResolver().insert(CONTENT_URI_RECIPES, cv);
                System.out.println("@@@korg");

                recipe_id = recipe.getId();
                ingredients = recipe.getIngredients();
                for(Ingredients in:ingredients){
                    cv.clear();

                    System.out.println("@@@@in.getIngredient()="+in.getIngredient());
                    System.out.println("@@@@in.getMeasure()="+in.getMeasure());
                    System.out.println("@@@@in.getQuantity()="+in.getQuantity());
                    System.out.println("@@@@recipe_id="+recipe_id);

                    cv.put(COLUMN_INGREDIENTS_INGREDIENT, in.getIngredient());
                    cv.put(COLUMN_INGREDIENTS_MEASURE, in.getMeasure());
                    cv.put(COLUMN_INGREDIENTS_QUANTITY, in.getQuantity());
                    cv.put(COLUMN_INGREDIENTS_RECIPE_ID, recipe_id);
                    context.getContentResolver().insert(CONTENT_URI_INGREDIENTS, cv);
                }

                steps = recipe.getSteps();
                for(Steps st:steps){
                    cv.clear();
                    cv.put(COLUMN_STEPS_DESCRIPTION, st.getDescription());
                    cv.put(COLUMN_STEPS_ID, st.getId());
                    cv.put(COLUMN_STEPS_SHORTDESCRIPTION, st.getShortDescription());
                    cv.put(COLUMN_STEPS_THUMBNAILURL, st.getThumbnailURL());
                    cv.put(COLUMN_STEPS_VIDEOURL, st.getVideoURL());
                    cv.put(COLUMN_STEPS_RECIPE_ID, recipe_id);
                    context.getContentResolver().insert(CONTENT_URI_STEPS, cv);
                }
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<RecipeModel[]> call, Throwable t) {
        System.out.println("fail=" + t.getMessage());
    }
}