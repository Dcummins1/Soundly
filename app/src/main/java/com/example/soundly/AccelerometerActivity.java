package com.example.soundly;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by conan on 06/11/2017.
 */

public class AccelerometerActivity extends Activity implements SensorEventListener {

        private float lastX, lastY, lastZ;

        // one minute in ms
        private final int timeSegment = 60000;

        private float averageX;
        private float averageY;
        private float averageZ;

        private long startTime;


        private SensorManager sensorManager;
        private Sensor accelerometer;

        private float deltaXMax = 0;
        private float deltaYMax = 0;
        private float deltaZMax = 0;

        private float deltaX = 0;
        private float deltaY = 0;
        private float deltaZ = 0;

        private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

        PowerManager pm;
        PowerManager.WakeLock wl;

        AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                //
            }
        };


    @Override
        public void onCreate(Bundle savedInstanceState) {

            // keeps running when screen is locked - **WARNING** just for testing.
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");


            super.onCreate(savedInstanceState);
            setContentView(R.layout.accelo_example);
            initializeViews();

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
                // success! we have an accelerometer

                accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                // fail! we don't have an accelerometer!
            }
            Date date = new Date();

            createOutputFile(date.toString() + "\nt,ms2(combined)\n");
            System.out.println(date.toString());
            startTime = System.currentTimeMillis();


        }

        public void createOutputFile(String data){
            String path = Environment.getExternalStorageDirectory().getPath() + File.separator  + "SoundlyOutput";
            System.out.println(path);
            // Create the folder.
            File folder = new File(path);
            System.out.println(folder.mkdirs());

            // Create the file.
            File file = new File(folder, "output.txt");
            // Save your stream, don't forget to flush() it before closing it.
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(data);

                myOutWriter.close();

                fOut.flush();
                fOut.close();
            }
            catch (IOException e)
            {
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }



        public void writeToTestFile(String string){
            try {
                FileOutputStream fOut = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + File.separator  + "SoundlyOutput/output.txt", true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append(string);

                myOutWriter.close();

                fOut.flush();
                fOut.close();
            }
            catch (IOException e){
                Log.e("Exception", "File write failed: " + e.toString());
            }

        }


        public void initializeViews() {
            currentX = (TextView) findViewById(R.id.currentX);
            currentY = (TextView) findViewById(R.id.currentY);
            currentZ = (TextView) findViewById(R.id.currentZ);

            maxX = (TextView) findViewById(R.id.maxX);
            maxY = (TextView) findViewById(R.id.maxY);
            maxZ = (TextView) findViewById(R.id.maxZ);
        }

        //onResume() register the accelerometer for listening the events
        protected void onResume() {
            super.onResume();
            if(wl.isHeld()){
                wl.release();
            }
//            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        //onPause() unregister the accelerometer for stop listening the events
        protected void onPause() {
            wl.acquire();
            super.onPause();
//            sensorManager.unregisterListener(this);
        }

        // method that pauses music on external apps.
        private void forceMusicStop() {
            System.out.println("STOPPING MUSIC");
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am.isMusicActive()) {
                System.out.println("MUSIC IS ACTIVE");
                am.requestAudioFocus(listener, am.STREAM_MUSIC, am.AUDIOFOCUS_GAIN);
//            Intent intentToStop = new Intent("com.sec.android.app.music.musicservicecommand");
//            intentToStop.putExtra("command", "pause");
//            this.sendBroadcast(intentToStop);
        }
    }


        protected void onStop(){
            wl.acquire();
            super.onStop();
//            wl.release();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            long currentTime = System.currentTimeMillis();


            if(currentTime - startTime > 60000){
//                forceMusicStop();
                float total = deltaXMax + deltaYMax + deltaZMax;
                System.out.println(currentTime + "," + total);
                writeToTestFile(currentTime + "," + total + "\n");
                deltaXMax = 0;
                deltaYMax = 0;
                deltaZMax = 0;
                startTime = currentTime;
            }

            // clean current values
            displayCleanValues();
            // display the current x,y,z accelerometer values
            displayCurrentValues();
            // display the max x,y,z accelerometer values
            displayMaxValues();

            // get the change of the x,y,z values of the accelerometer
            deltaX = Math.abs(lastX - event.values[0]);
            deltaY = Math.abs(lastY - event.values[1]);
            deltaZ = Math.abs(lastZ - event.values[2]);

             //save values of x,y,z for next comparison
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];

            // ignore small changes
//            if (deltaX < 2)
//                deltaX = 0;
//            if (deltaY < 2)
//                deltaY = 0;
//            if (deltaZ < 2)
//                deltaX = 0;

        }

        public void displayCleanValues() {
            currentX.setText("0.0");
            currentY.setText("0.0");
            currentZ.setText("0.0");
        }

        // display the current x,y,z accelerometer values
        public void displayCurrentValues() {
            currentX.setText(Float.toString(deltaX));
            currentY.setText(Float.toString(deltaY));
            currentZ.setText(Float.toString(deltaZ));
        }

        // display the max x,y,z accelerometer values
        public void displayMaxValues() {
            if (deltaX > deltaXMax) {
                deltaXMax = deltaX;
                maxX.setText(Float.toString(deltaXMax));
            }
            if (deltaY > deltaYMax) {
                deltaYMax = deltaY;
                maxY.setText(Float.toString(deltaYMax));
            }
            if (deltaZ > deltaZMax) {
                deltaZMax = deltaZ;
                maxZ.setText(Float.toString(deltaZMax));
            }
        }
    }



