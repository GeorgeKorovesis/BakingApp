package com.example.korg.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
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
    private NetworkReceiver networkReceiver;
    private static final int LOADER_ID = 1;
    private static final int GRID_COLS = 3;
    private static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String NO_CONNECTIVITY_MESSAGE ="NO INTERNET CONNECTION";

    SharedPreferences sharedPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        boolean isOnline = Network.isOnline(getActivity());

        /*Fetch data if there is intenret connectivity and data have not been fetched before*/
        if (isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
            RetrofitController controller = new RetrofitController(getActivity());
            controller.start();
            sharedPrefs.edit().putBoolean(getString(R.string.recipes_fetched), true).apply();
        }
        else
        {
            networkReceiver = new NetworkReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CONNECTIVITY_ACTION);
            getActivity().registerReceiver(networkReceiver,filter);
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

        boolean isOnline = Network.isOnline(getActivity());
        if (!isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
           Snackbar.make(getActivity().findViewById(R.id.main_id),NO_CONNECTIVITY_MESSAGE, Snackbar.LENGTH_LONG).show();
        }

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
    public void onDestroy() {
        super.onDestroy();

        if(networkReceiver!=null){
            getActivity().unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
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

    private class NetworkReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isOnline = Network.isOnline(getActivity());
        /*Fetch data if there is intenret connectivity and data have not been fetched before*/
            if (isOnline && !sharedPrefs.getBoolean(getString(R.string.recipes_fetched), false)) {
                RetrofitController controller = new RetrofitController(getActivity());
                controller.start();
                sharedPrefs.edit().putBoolean(getString(R.string.recipes_fetched), true).apply();
            }
        }
    }

}