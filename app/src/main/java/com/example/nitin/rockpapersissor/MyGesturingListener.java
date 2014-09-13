package com.example.nitin.rockpapersissor;

import android.content.Context;
import android.gesture.GestureOverlayView;

/**
 * Created by nitin on 9/12/14.
 */
public class MyGesturingListener implements GestureOverlayView.OnGesturingListener {

    Context context=null;
    MyGesturingListener(Context context)
    {
        this.context=context;
    }
    @Override
    public void onGesturingStarted(GestureOverlayView gestureOverlayView) {


    }

    @Override
    public void onGesturingEnded(GestureOverlayView gestureOverlayView) {

    }

}
