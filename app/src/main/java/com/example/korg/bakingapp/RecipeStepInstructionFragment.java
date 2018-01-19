package com.example.korg.bakingapp;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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


import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_DESCRIPTION;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_VIDEOURL;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.CONTENT_URI_STEPS;


public class RecipeStepInstructionFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String RECIPE_ID = "recipe id";
    private final static String STEPS_ID = "steps id";
    private final static String VIDEO_POS = "video position";
    private static final int LOADER_ID = 4;

    private int recipeId;
    private int recipeStepId;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;

    private TextView description;

    private Cursor data;
    private int dataSize;
    private static long videoPosition;

    public RecipeStepInstructionFragment() {
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
        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_ID) && savedInstanceState.containsKey(STEPS_ID) && savedInstanceState.containsKey(VIDEO_POS)) {
            recipeStepId = savedInstanceState.getInt(STEPS_ID);
            recipeId = savedInstanceState.getInt(RECIPE_ID);
            videoPosition = savedInstanceState.getLong(VIDEO_POS);
        } else {
            if (getArguments() != null) {
                recipeStepId = getArguments().getInt(STEPS_ID);
                recipeId = getArguments().getInt(RECIPE_ID);
                videoPosition = 0;
            }
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
            data.move(recipeStepId);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.seekTo(videoPosition);
            exoPlayerView.setPlayer(exoPlayer);

            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            if (description != null)
                description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
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
        if (data != null) {
            this.data = data;
            this.dataSize = data.getCount();
            data.moveToPosition(recipeStepId);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.seekTo(videoPosition);
            exoPlayer.setPlayWhenReady(true);
            exoPlayerView.setPlayer(exoPlayer);

            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

            if (description != null)
                description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
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

        recipeStepId = (recipeStepId + 1) % dataSize;

        if (data != null) {
            data.moveToPosition(recipeStepId);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));
            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            if (description != null)
                description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }

    }

    public void previousClicked() {

        if (recipeStepId > 0)
            recipeStepId--;
        else
            recipeStepId = dataSize - 1;

        if (data != null) {
            data.moveToPosition(recipeStepId);
            String url = data.getString(data.getColumnIndex(COLUMN_STEPS_VIDEOURL));

            exoPlayer = preparePlayer(exoPlayer, Uri.parse(url));
            exoPlayer.setPlayWhenReady(true);

            exoPlayerView.setPlayer(exoPlayer);
            exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            if (description != null)
                description.setText(data.getString(data.getColumnIndex(COLUMN_STEPS_DESCRIPTION)));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exoPlayer.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID, recipeId);
        outState.putInt(STEPS_ID, recipeStepId);
        outState.putLong(VIDEO_POS, exoPlayer.getContentPosition());
        super.onSaveInstanceState(outState);
    }


}
