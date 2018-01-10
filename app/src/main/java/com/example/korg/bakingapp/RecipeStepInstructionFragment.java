package com.example.korg.bakingapp;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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
/*
TODO : fragment to receive COLUMN_STEPS_RECIPE_ID only and base on that to do its work
  When next is pressed, player will be reinitiated with new url and description will be changed.
  keep current step_id...also buttons next and previous shall be checked upon whether is the first (no prev)
  or last (no next)
*/

    private final static String RECIPE_ID = "recipe id";
    private final static String STEPS_ID = "steps id";
    private static final int GRID_COLS = 3;
    private static final int LOADER_ID = 4;

    private int recipeId;
    private int recipeStepId;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    private TextView description;

    private Cursor data;
    private int dataLength;

    private int currentCursorPos = 0;

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

        description = rootView.findViewById(R.id.description);

        exoPlayerView = rootView.findViewById(R.id.exoplayer);
        exoPlayer = createExoPlayer();
        if (data != null) {
            data.move(currentCursorPos);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
//        exoPlayer.setPlayWhenReady(true);


        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {COLUMN_STEPS_DESCRIPTION, COLUMN_STEPS_ID, COLUMN_STEPS_VIDEOURL};
        String selection = COLUMN_STEPS_RECIPE_ID.concat("=?");
        String[] selectionArgs = {String.valueOf(recipeId)};

        return new CursorLoader(getActivity(), CONTENT_URI_STEPS, projection,
                selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        this.data = data;
        this.dataLength = data.getCount();
        if (data != null) {
            data.moveToPosition(currentCursorPos);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
        System.out.println("@@@length is " + dataLength);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public SimpleExoPlayer createExoPlayer() {
        //TrackSelector is created
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        //Player is created
        return ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
    }

    public SimpleExoPlayer preparePlayer(SimpleExoPlayer player, Uri videoUrl) {


        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), getActivity().getPackageName()), bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUrl);
        // Prepare the player with the source.
        player.prepare(videoSource);
        return player;
    }

    public void nextClicked() {

        currentCursorPos = (currentCursorPos + 1) % dataLength;
        System.out.println("@@@@Next clicked - pos=" + currentCursorPos);

        if (data != null) {
            data.moveToPosition(currentCursorPos);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            System.out.println("@@@url = " + url);
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
        //exoPlayerView.setPlayer(exoPlayer);
        //exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

    }

    public void previousClicked() {

        if (currentCursorPos > 0)
            currentCursorPos--;
        else
            currentCursorPos = dataLength - 1;


        System.out.println("@@@@Next clicked");
        if (data != null) {
            data.moveToPosition(currentCursorPos);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            System.out.println("@@@url = " + url);

            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
    }
}
