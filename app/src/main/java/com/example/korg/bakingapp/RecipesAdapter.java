package com.example.korg.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_NAME;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_DESCRIPTION;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_RECIPE_ID;

/**
 * Created by korg on 26/12/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private Cursor recipesCursor;
    private ActivityNotification callback;
    private String fragment;
    private String card;
    private Context context;

    final static String bakingTimeFragment = "BakingTimeFragment";
    final static String recipeNameFragment = "RecipeNameFragment";
    final static String recipeCard = "RecipeCard";
    final static String recipeIngredientsCard = "RecipeIngredientsCard";
    final static String recipeStepDescriptionCard = "RecipeStepDescriptionCard";


    RecipesAdapter(Object object, Cursor recipesCursor, String fragment, String card) {
        context = (Context) object;
        callback = (ActivityNotification) object;
        this.recipesCursor = recipesCursor;
        this.fragment = fragment;
        this.card = card;
    }

    void replaceData(Cursor recipesCursor){
        this.recipesCursor = recipesCursor;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_row, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        switch (fragment) {
            case bakingTimeFragment: {
                if (recipesCursor != null) {
                    recipesCursor.moveToPosition(position);
                    holder.recipe.setText(recipesCursor.getString(recipesCursor.getColumnIndex(COLUMN_RECIPES_NAME)));
                    final int id = recipesCursor.getInt(recipesCursor.getColumnIndex(COLUMN_RECIPES_ID));
                    holder.recipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.notifyActivity(bakingTimeFragment, recipeCard, id);
                        }
                    });
                }
                break;
            }
            case recipeNameFragment: {
                if (position == 0) {
                    holder.recipe.setText(context.getString(R.string.recipe_ingredients));
                    recipesCursor.moveToPosition(position);

                    /*return recipe id*/
                    final int id = recipesCursor.getInt(recipesCursor.getColumnIndex(COLUMN_STEPS_RECIPE_ID));

                    holder.recipe.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callback.notifyActivity(recipeNameFragment, recipeIngredientsCard, id);
                        }
                    });
                } else {
                    if (recipesCursor != null) {
                        recipesCursor.moveToPosition(position - 1);
                        holder.recipe.setText(recipesCursor.getString(recipesCursor.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));

                        final int id = recipesCursor.getInt(recipesCursor.getColumnIndex(COLUMN_STEPS_ID));

                        holder.recipe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callback.notifyActivity(recipeNameFragment, recipeStepDescriptionCard, id);
                            }
                        });
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {

        int additionalItem = 0;

        /*add one to the count, since first card is related to Recipe Ingredients*/
        if (fragment.equals(recipeNameFragment))
            additionalItem++;

            if (recipesCursor != null)
                return (recipesCursor.getCount() + additionalItem);
            else
                return 0;
        }

        class RecipeViewHolder extends RecyclerView.ViewHolder {
            TextView recipe;

            RecipeViewHolder(View itemView) {
                super(itemView);
                recipe = itemView.findViewById(R.id.recipe);
            }
        }
    }