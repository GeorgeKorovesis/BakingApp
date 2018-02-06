package com.example.korg.bakingapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_IMAGE;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_NAME;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_RECIPES;
import static com.example.korg.bakingapp.RecipesAdapter.bakingTimeFragment;
import static com.example.korg.bakingapp.RecipesAdapter.recipeCard;


public class RecipeNameFragment extends Fragment implements LoaderCallbacks<Cursor> {

    private RecipesAdapter recipesAdapter;
    private NetworkReceiver networkReceiver;
    private static final int LOADER_ID = 1;
    private static final int GRID_COLS_3 = 3;
    private static final int GRID_COLS_1 = 3;
    private static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String NO_CONNECTIVITY_MESSAGE = "NO INTERNET CONNECTION";
    private ActivityNotification activity;
    private SharedPreferences sharedPrefs;
    private RecyclerView recView;
    private Cursor data;

    private boolean isTablet;

    public RecipeNameFragment() {
    }

    public static RecipeNameFragment newInstance() {
        return new RecipeNameFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isTablet = getActivity().getResources().getBoolean(R.bool.isTablet);
        boolean isOnline = Network.isOnline(getActivity());

        /*Fetch data if there is internet connectivity and data have not been fetched before*/
        if (isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
            RetrofitController controller = new RetrofitController(getActivity());
            controller.start();
            sharedPrefs.edit().putBoolean(getString(R.string.recipes_fetched), true).apply();
        } else {
            networkReceiver = new NetworkReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CONNECTIVITY_ACTION);
            getActivity().registerReceiver(networkReceiver, filter);
        }

        getActivity().getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = (ActivityNotification) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_name, container, false);

        recView = rootView.findViewById(R.id.recview);
        RecyclerView.LayoutManager layout;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && !isTablet)
            layout = new GridLayoutManager(getActivity(), GRID_COLS_1);
        else
            layout = new GridLayoutManager(getActivity(), GRID_COLS_3);

        recView.setLayoutManager(layout);

        recipesAdapter = new RecipesAdapter(getActivity(), data, bakingTimeFragment, recipeCard);
        recView.setAdapter(recipesAdapter);

        boolean isOnline = Network.isOnline(getActivity());
        if (!isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
            Snackbar.make(getActivity().findViewById(R.id.main_id), NO_CONNECTIVITY_MESSAGE, Snackbar.LENGTH_LONG).show();
        }

        activity.updateActionBar("Recipes");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onDestroy() {
        super.onDestroy();

        if (networkReceiver != null) {
            getActivity().unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
    }

    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        String[] projection = {COLUMN_RECIPES_ID, COLUMN_RECIPES_NAME, COLUMN_RECIPES_IMAGE};

        return new CursorLoader(getActivity(), CONTENT_URI_RECIPES, projection,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.data = data;
        if (recipesAdapter != null) {
            recipesAdapter.replaceData(data);
            recipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        this.data = null;
        recipesAdapter.replaceData(null);
        recipesAdapter.notifyDataSetChanged();
    }


    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOnline = Network.isOnline(getActivity());

        /*Fetch data if there is internet connectivity and data have not been fetched before*/
            if (isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
                RetrofitController controller = new RetrofitController(getActivity());
                controller.start();
                sharedPrefs.edit().putBoolean(getString(R.string.recipes_fetched), true).apply();
            }
        }
    }

}