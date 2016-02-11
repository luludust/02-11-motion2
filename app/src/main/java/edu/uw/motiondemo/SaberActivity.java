package edu.uw.motiondemo;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class SaberActivity extends Activity {

    private static final String TAG = "**SABER**";

    //TODO: Add instance variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saber);

        initializeSoundPool();
    }

    //helper method for setting up the sound pool
    @SuppressWarnings("deprecation")
    private void initializeSoundPool(){
        //TODO: Create the SoundPool

        //TODO: Load the sounds
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case MotionEvent.ACTION_DOWN:
                handleTap(event.getX(), event.getY());
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    //helper method for handling tap logic
    public void handleTap(double x, double y){
        View view = findViewById(R.id.saberView);
        int width = view.getWidth();
        int height = view.getHeight();

        int quadrant;
        if(x > width/2 && y < height/2) quadrant = 1;
        else if(x < width/2 && y < height/2) quadrant = 2;
        else if(x < width/2 && y > height/2) quadrant = 3;
        else quadrant = 4;

        Log.v(TAG, "Tap in quadrant: "+quadrant);

        //TODO: Play sound depending on quadrant!

    }

    //for immersive full-screen (from API guide)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            final View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);}
    }
}
