package com.example.soundly;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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

        private float lastTotal1 = 5;
        private float lastTotal2 = 5;
        private float lastTotal3 = 5;

        private int sensitivity = 1;
        private double sleepThreshold = 0.15;

        private boolean saveFile = true;


        private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

        PowerManager pm;
        PowerManager.WakeLock wl;
        DevicePolicyManager devicePolicyManager;
        ComponentName componentName;
        SharedPreferences sharedPreferences;


        AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                //
            }
        };


        @Override
        public void onCreate(Bundle savedInstanceState) {

            // keeps running when screen is locked
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

            if(! wl.isHeld()){
                Toast.makeText(this, "making file", Toast.LENGTH_LONG).show();

                createOutputFile(date.toString() + "\n");
//                System.out.println(date.toString());
            }

            startTime = System.currentTimeMillis();
            devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = new ComponentName(this, AdminReceiver.class);
            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        }

        public void getPreferences(){
            this.sensitivity = sharedPreferences.getInt("sensitivity", this.sensitivity);
            this.saveFile = sharedPreferences.getBoolean("saveFile", this.saveFile);
            System.out.println("Sensitivity at: " + this.sensitivity);
            System.out.println("Save file at: " + this.saveFile);

        }

        public void updateSleepThreshold(){
            if(this.sensitivity == 0){
                this.sleepThreshold =  0.2;
            }
            else if(this.sensitivity == 1){
                this.sleepThreshold = 0.15;
            }
            else if(this.sensitivity == 2){
                this.sleepThreshold = 0.1;
            }
            System.out.println("Threshold at: " + this.sleepThreshold);
        }

        public void createOutputFile(String data){
            String fileName = "soundly_output.txt";
            Context context = getApplicationContext();
            String yourFilePath = context.getFilesDir() + "/" + "soundly_output.txt";

            File yourFile = new File( yourFilePath );

            if (yourFile.exists()) {
                System.out.println("file exists");
            }




            try{
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                //fos.write(data.getBytes());

                OutputStreamWriter myOutWriter = new OutputStreamWriter(fos);
                myOutWriter.write(data);

                myOutWriter.close();

                fos.flush();
                fos.close();
            }
            catch (IOException e)
            {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            for(String file : fileList()){
                System.out.println(file);

            }

        }



        public void writeToTestFile(String string){
            try {
                System.out.println(getFilesDir()+File.separator+"soundly_output.txt");
                //fout = openFileOutput(Environment.getDataDirectory() + File.separator  + "soundly_output.txt", Context.MODE_APPEND);
                FileOutputStream fOut = new FileOutputStream(getFilesDir()+File.separator+"soundly_output.txt", true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.write(string);

                myOutWriter.close();

                fOut.flush();
                fOut.close();
//                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(getFilesDir()+File.separator+"soundly_output.txt")));
//                bufferedWriter.append(string);
//                 bufferedWriter.close();
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
            checkLockPermission();
            getPreferences();
            updateSleepThreshold();
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

        // checks if app has permission required to lock screen
        // if not, requests permission
        public void checkLockPermission() {
            System.out.println("Checking permissions...");
            if (devicePolicyManager.isAdminActive(componentName)) {
                System.out.println("Permissions active...");
            }
            else {
                String device_admin_explanation = "We need your permission to lock your device. This allows Soundly to pause certain media and save your battery.";
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, device_admin_explanation);
                startActivity(intent);
            }
        }

        // method that pauses music on external apps and locks screen.
        private void forceMusicStop() {
            System.out.println("STOPPING MUSIC");
            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (am.isMusicActive()) {
                System.out.println("MUSIC IS ACTIVE");
                am.requestAudioFocus(listener, am.STREAM_MUSIC, am.AUDIOFOCUS_GAIN);
                if(devicePolicyManager.isAdminActive(componentName)) {
//                    System.out.println("Locking screen");
                    wl.acquire();
                    devicePolicyManager.lockNow();
                }
                else {
//                    System.out.println("No permission...");
                    checkLockPermission();
                }
                if(!saveFile){
                    System.out.println("Killing Soundly");
                    System.out.println("PID is: " + android.os.Process.myPid());
                    android.os.Process.killProcess(android.os.Process.myPid());
//                    System.exit(1);
                }
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


            if(currentTime - startTime > 30000){  //60000
                forceMusicStop();
                float total = deltaXMax + deltaYMax + deltaZMax;
                System.out.println(currentTime + "," + total);
                writeToTestFile(currentTime + "," + total + "\n");
//                readFile();
                deltaXMax = 0;
                deltaYMax = 0;
                deltaZMax = 0;
                startTime = currentTime;
                lastTotal3 = lastTotal2;
                lastTotal2 = lastTotal1;
                lastTotal1 = total;

                if(lastTotal1 < sleepThreshold && lastTotal2 < sleepThreshold && lastTotal3 < sleepThreshold){
                    // user is asleep
                    forceMusicStop();
                }

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



