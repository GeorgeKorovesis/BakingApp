package com.example.korg.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_ID;
import static com.example.korg.bakingapp.BakingContract.BakingEntry.COLUMN_RECIPES_NAME;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {


    private static final String ACTION_NEXT_CLICK =
            "com.example.korg.bakingapp.NEXT_CLICK";
    private static final String ACTION_PREV_CLICK =
            "com.example.korg.bakingapp.PREV_CLICK";
    private static final String ID = "ID";
    private static int id = 1, count;
    private final String ACTION_UPDATE_WIDGET = "com.example.korg.bakingapp.UPDATE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(
                    context.getPackageName(),
                    R.layout.app_widget
            );

            Uri uri = BakingContract.BakingEntry.CONTENT_URI_RECIPES;
            String[] projection = {COLUMN_RECIPES_NAME};


            Cursor mCursor = context.getContentResolver().query(uri,
                    projection,
                    null,
                    null,
                    null);

            if (mCursor != null) {
                count = mCursor.getCount();
                mCursor.moveToFirst();
                String text = mCursor.getString(mCursor.getColumnIndex(COLUMN_RECIPES_NAME));
                views.setTextViewText(R.id.widgetItemTaskNameLabel, text);
                mCursor.close();
            }

            Intent intent = new Intent(context, MyWidgetRemoteViewsService.class);
            intent.putExtra(ID, id);

            views.setOnClickPendingIntent(R.id.previous, getPendingSelfIntent(context, ACTION_PREV_CLICK));
            views.setOnClickPendingIntent(R.id.next, getPendingSelfIntent(context, ACTION_NEXT_CLICK));
            views.setRemoteAdapter(R.id.widgetListView, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, AppWidget.class));
        RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        Cursor cursor;

        if (ACTION_PREV_CLICK.equals(intent.getAction())) {

            if (id == 1)
                id = count;
            else
                id--;

            sendBroadcast(context, id);

            cursor = fetchCursor(context);

            if (cursor != null) {
                cursor.moveToFirst();
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPES_NAME));
                remoteView.setTextViewText(R.id.widgetItemTaskNameLabel, text);

                appWidgetManager.updateAppWidget(appWidgetIds, remoteView);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
                cursor.close();
            }
        } else if (ACTION_NEXT_CLICK.equals(intent.getAction())) {

            if (id == count)
                id = 1;
            else
                id++;

            sendBroadcast(context, id);

            cursor = fetchCursor(context);

            if (cursor != null) {
                cursor.moveToFirst();
                String text = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPES_NAME));
                remoteView.setTextViewText(R.id.widgetItemTaskNameLabel, text);

                appWidgetManager.updateAppWidget(appWidgetIds, remoteView);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
                cursor.close();
            }
        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void sendBroadcast(Context context, int id) {
        Intent i = new Intent();
        i.setAction(ACTION_UPDATE_WIDGET);
        i.putExtra(ID, id);
        context.sendBroadcast(i);
    }

    private Cursor fetchCursor(Context context) {
        Uri uri = BakingContract.BakingEntry.CONTENT_URI_RECIPES;
        String[] projection = {COLUMN_RECIPES_NAME};
        String selection = COLUMN_RECIPES_ID.concat("=?");
        String[] selectionArgs = {String.valueOf(id)};

        return context.getContentResolver().query(uri,
                projection,
                selection,
                selectionArgs,
                null);
    }
}