package com.example.korg.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_INGREDIENTS_INGREDIENT;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_INGREDIENTS;


public class RecipeIngredientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ID = "id";
    private static final int GRID_COLS = 3;
    private static final int LOADER_ID = 3;
    private int recipeId;
    private RecyclerView recView;
    private RecipesAdapter recipesAdapter;
    private TextView ingredients;
    private Cursor data;

    public RecipeIngredientsFragment() {
    }

    public static RecipeIngredientsFragment newInstance(int id) {
        RecipeIngredientsFragment fragment = new RecipeIngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(ID))
            recipeId = savedInstanceState.getInt(ID);
        else {
            if (getArguments() != null)
                recipeId = getArguments().getInt(ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients, container, false);
        ingredients = rootView.findViewById(R.id.ingredients);
        if(data!=null)
            ingredients.setText(createIngredientsText());

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = COLUMN_INGREDIENTS_RECIPE_ID.concat("=?");
        String[] selectionArgs = {String.valueOf(recipeId)};

        return new CursorLoader(getActivity(), CONTENT_URI_INGREDIENTS, null,
                selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.data = data;
        if(ingredients!=null)
            ingredients.setText(createIngredientsText());
//        recipesAdapter.replaceData(data);
//        recipesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        data = null;
//        recipesAdapter.replaceData(null);
//        recipesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ID, recipeId);
        super.onSaveInstanceState(outState);
    }

    private String createIngredientsText()
    {
        String ingredientsText="";
        data.moveToFirst();
        while(data.moveToNext()){
            ingredientsText += (data.getString(data.getColumnIndex(COLUMN_INGREDIENTS_INGREDIENT))+"\n");
        }
        return ingredientsText;
    }
}
