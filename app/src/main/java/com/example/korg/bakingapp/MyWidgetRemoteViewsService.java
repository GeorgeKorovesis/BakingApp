package com.example.korg.bakingapp;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by korg on 17/1/2018.
 */

public class MyWidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}