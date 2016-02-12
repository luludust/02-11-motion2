package edu.uw.motiondemo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class SaberActivity extends Activity implements SensorEventListener {

    private static final String TAG = "**SABER**";

    private SoundPool mSoundPool;
    private int[] soundIds;
    private boolean[] loadedSound;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saber);

        initializeSoundPool();

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

//        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//        for(Sensor sensor : sensors){
//            Log.v(TAG, sensor+"");
//        }

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(mAccelerometer == null) { //we don't have one
            Log.v(TAG, "No accelerometer");
            finish();
        }
    }

    @Override
    protected void onResume() {
        //register sensor
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //unregister sensor
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(Math.abs(event.values[0]) > 2.0){
            Log.v(TAG, "Shook left: "+event.values[0]);
            playSound(1);
        }
        else if(Math.abs(event.values[1]) > 2.0){
            Log.v(TAG, "Shook up: "+event.values[1]);
            playSound(2);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //leave blank for now
    }


    //helper method for setting up the sound pool
    @SuppressWarnings("deprecation")
    private void initializeSoundPool(){

        final int MAX_STREAMS = 4;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attribs = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            mSoundPool = new SoundPool.Builder()
                    .setMaxStreams(MAX_STREAMS)
                    .setAudioAttributes(attribs)
                    .build();
        } else {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        soundIds = new int[5];
        loadedSound = new boolean[5];

        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    if(sampleId == soundIds[0]) {loadedSound[0] = true; playSound(0);}
                    else if(sampleId == soundIds[1]) loadedSound[1] = true;
                    else if(sampleId == soundIds[2]) loadedSound[2] = true;
                    else if(sampleId == soundIds[3]) loadedSound[3] = true;
                    else if(sampleId == soundIds[4]) loadedSound[4] = true;
                }
            }
        });

        soundIds[0] = mSoundPool.load(this, R.raw.saber_on, 0);
        soundIds[1] = mSoundPool.load(this, R.raw.saber_swing1, 0);
        soundIds[2] = mSoundPool.load(this, R.raw.saber_swing2, 0);
        soundIds[3] = mSoundPool.load(this, R.raw.saber_swing3, 0);
        soundIds[4] = mSoundPool.load(this, R.raw.saber_swing4, 0);
    }

    public void playSound(int index){
        if(loadedSound[index]){
            mSoundPool.play(soundIds[index],1,1,1,0,1);
        }
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

        playSound(quadrant);

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