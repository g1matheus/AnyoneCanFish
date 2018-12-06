package com.homebrewforlife.sharkydart.anyonecanfish.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class FishingWidgetRVService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //return remote view factory here
        return new FishingWidgetRVFactory(this, intent);
    }

}
