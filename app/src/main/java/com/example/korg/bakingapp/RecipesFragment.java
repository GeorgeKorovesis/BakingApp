package com.example.korg.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_RECIPES;


public class RecipesFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private RecipesAdapter recipesAdapter;
    private RecyclerView recView;
    private static final int LOADER_ID = 1;
    private static final int GRID_COLS = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (!sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
            RetrofitController controller = new RetrofitController(getActivity());
            controller.start();
            sharedPrefs.edit().putBoolean(getString(R.string.recipes_fetched), true).apply();
        }
        getActivity().getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);
        recView = rootView.findViewById(R.id.recview);

        RecyclerView.LayoutManager layout;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            layout = new LinearLayoutManager(getActivity());
        else
            layout = new GridLayoutManager(getActivity(), GRID_COLS);

        recView.setLayoutManager(layout);
        recipesAdapter = new RecipesAdapter(getActivity(), null);
        recView.setAdapter(recipesAdapter);

        return rootView;
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
    public CursorLoader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), CONTENT_URI_RECIPES, null,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        RecipesAdapter recipeAdapter = new RecipesAdapter(getActivity(), data);
        recipeAdapter.notifyDataSetChanged();
        recView.invalidate();
        recView.setAdapter(recipeAdapter);
        recipeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipesAdapter = new RecipesAdapter(getActivity(), null);
        recView.setAdapter(recipesAdapter);
    }
}