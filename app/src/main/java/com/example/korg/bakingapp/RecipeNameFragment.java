package com.example.korg.bakingapp;

import android.app.LoaderManager;
import android.content.Context;
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
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_STEPS;
import static com.example.korg.bakingapp.RecipesAdapter.recipeCard;
import static com.example.korg.bakingapp.RecipesAdapter.recipeNameFragment;


public class RecipeNameFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 2;
    private static final int GRID_COLS_LAND = 3;
    private static final int GRID_COLS_PORT = 1;
    private int recipeId;
    private static final String ID = "recipe_id";
    private RecyclerView recView;
    private RecipesAdapter recipesAdapter;
    private LoaderManager manager;

    public RecipeNameFragment() {
    }

    public static RecipeNameFragment newInstance(int id) {
        RecipeNameFragment fragment = new RecipeNameFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipeId = getArguments().getInt(ID, 0);
        manager = getActivity().getLoaderManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        recView = rootView.findViewById(R.id.recview);

        RecyclerView.LayoutManager layout;

        int columns;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            columns = GRID_COLS_PORT;
        else
            columns = GRID_COLS_LAND;

        layout = new GridLayoutManager(getActivity(), columns);

        recView.setLayoutManager(layout);
        recipesAdapter = new RecipesAdapter(getActivity(), null, recipeNameFragment, recipeCard);
        recView.setAdapter(recipesAdapter);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        manager.restartLoader(LOADER_ID, null, this);
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

        if (id == LOADER_ID) {
            String selection = COLUMN_INGREDIENTS_RECIPE_ID.concat("=?");
            String[] selectionArgs = {String.valueOf(recipeId)};

            return new CursorLoader(getActivity(), CONTENT_URI_STEPS, null,
                    selection, selectionArgs, null);
        } else
            return null;
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
