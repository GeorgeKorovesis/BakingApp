package com.example.korg.bakingapp;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_INGREDIENTS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_NAME;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_DESCRIPTION;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_SHORTDESCRIPTION;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_THUMBNAILURL;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_VIDEOURL;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_INGREDIENTS;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_STEPS;
import static com.example.korg.bakingapp.RecipesAdapter.recipeIngredientsCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeStepCard;


public class RecipeStepInstructionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String RECIPE_ID = "recipe id";
    private final static String STEPS_ID = "steps id";
    private static final int GRID_COLS = 3;
    private static final int LOADER_ID = 4;

    private int recipeId;
    private int recipeStepId;
    private RecyclerView recView;
    private RecipesAdapter recipesAdapter;

    public RecipeStepInstructionFragment() {
        // Required empty public constructor
    }

    public static RecipeStepInstructionFragment newInstance(int recipeId, int stepsId) {
        RecipeStepInstructionFragment fragment = new RecipeStepInstructionFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, recipeId);
        args.putInt(STEPS_ID, stepsId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeStepId = getArguments().getInt(STEPS_ID);
            recipeId = getArguments().getInt(RECIPE_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_instruction, container, false);
        //recView = rootView.findViewById(R.id.recview);

        RecyclerView.LayoutManager layout;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            layout = new LinearLayoutManager(getActivity());
        else
            layout = new GridLayoutManager(getActivity(), GRID_COLS);

        //recView.setLayoutManager(layout);
        recipesAdapter = new RecipesAdapter(getActivity(), null, recipeNameFragment, recipeStepCard);
        //recView.setAdapter(recipesAdapter);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {COLUMN_STEPS_DESCRIPTION, COLUMN_STEPS_VIDEOURL};
        String selection = COLUMN_STEPS_ID.concat("=?").concat("AND ").concat(COLUMN_STEPS_RECIPE_ID).concat("=?");
        String[] selectionArgs = {String.valueOf(recipeStepId),String.valueOf(recipeId)};

        return new CursorLoader(getActivity(), CONTENT_URI_STEPS, projection,
                selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        recipesAdapter.replaceData(data);
        recipesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipesAdapter.replaceData(null);
        recipesAdapter.notifyDataSetChanged();
    }
}
