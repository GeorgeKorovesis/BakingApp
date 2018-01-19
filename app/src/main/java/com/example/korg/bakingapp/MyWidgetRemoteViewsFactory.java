package com.example.korg.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_RECIPE_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_STEPS_SHORTDESCRIPTION;

/**
 * Created by korg on 17/1/2018.
 */

public class MyWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;
    private int id;
    private BroadcastReceiver mIntentListener;
    private final String ACTION_UPDATE_WIDGET = "com.example.korg.bakingapp.UPDATE_WIDGET";

    private static final String ID = "ID";


    MyWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        id = intent.getIntExtra("ID", 0);
    }

    @Override
    public void onCreate() {
        setupIntentListener();

    }

    @Override
    public void onDataSetChanged() {

        if (mCursor != null)
            mCursor.close();

        Uri uri = BakingContract.BakingEntry.CONTENT_URI_STEPS;
        String[] projection = {COLUMN_STEPS_SHORTDESCRIPTION};
        String selection = COLUMN_STEPS_RECIPE_ID.concat("=?");
        String[] selectionArgs = {String.valueOf(id)};

        mCursor = mContext.getContentResolver().query(uri,
                projection,
                selection,
                selectionArgs,
                BakingContract.BakingEntry._ID + " ASC");
    }

    @Override
    public void onDestroy() {
        if (mCursor != null)
            mCursor.close();

        teardownIntentListener();
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION ||
                mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        rv.setTextViewText(R.id.widgetRecipeSteps, mCursor.getString(mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEPS_SHORTDESCRIPTION)));

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return mCursor.moveToPosition(position) ? mCursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void setupIntentListener() {
        if (mIntentListener == null) {
            mIntentListener = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Update mUrl through BroadCast Intent
                    id = intent.getIntExtra(ID, 1);
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_UPDATE_WIDGET);
            mContext.registerReceiver(mIntentListener, filter);
        }
    }

    private void teardownIntentListener() {
        if (mIntentListener != null) {
            mContext.unregisterReceiver(mIntentListener);
            mIntentListener = null;
        }
    }

}
