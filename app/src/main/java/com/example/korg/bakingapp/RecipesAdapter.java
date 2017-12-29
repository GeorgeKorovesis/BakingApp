package com.example.korg.bakingapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_NAME;

/**
 * Created by korg on 26/12/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    Cursor recipesCursor;
    Context context;

    public RecipesAdapter(Context context, Cursor recipesCursor) {
        this.context = context;
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
        if (recipesCursor != null) {
            recipesCursor.moveToPosition(position);
            holder.recipe.setText(recipesCursor.getString(recipesCursor.getColumnIndex(COLUMN_RECIPES_NAME)));
        }
    }

    @Override
    public int getItemCount() {
        if (recipesCursor != null)
            return recipesCursor.getCount();
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
