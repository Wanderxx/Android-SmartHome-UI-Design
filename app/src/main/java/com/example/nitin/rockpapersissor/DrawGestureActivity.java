package com.example.nitin.rockpapersissor;

import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class DrawGestureActivity extends ActionBarActivity {


    private static GestureLibrary gestureLibrary;
    private static GestureOverlayView overlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_gesture);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.draw_gesture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment  {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            String TAG=this.getClass().getSimpleName().toString();

            View rootView = inflater.inflate(R.layout.fragment_draw_gesture, container, false);

            gestureLibrary= GestureLibraries.fromRawResource(getActivity(), R.raw.gestures);
            overlay = (GestureOverlayView)rootView.findViewById(R.id.gestures_draw);
            //being done this way as I was unable to pass 'this' object to methods used for registering listeners
            overlay.addOnGesturingListener(new MyGesturingListener(getActivity()));
            overlay.addOnGesturePerformedListener(new MyGesturePerformedListener(getActivity(),gestureLibrary));

            if(gestureLibrary==null)
            {
                Log.e(TAG,"Gestures file not found");
            }
            else{

                if(!gestureLibrary.load()){
                    Log.e(TAG,"Error loading gestures from file");
                    getActivity().finish();
                }
            }

            Button playAgain= (Button)rootView.findViewById(R.id.button_play_again);
            playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overlay.clear(false);
                }
            });

            return rootView;
        }
    }
}
