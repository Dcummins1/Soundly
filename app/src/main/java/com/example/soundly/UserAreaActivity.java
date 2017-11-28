package com.example.soundly;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

//CONAN PASTE OPEN
//import android.app.Activity;
import android.app.admin.DevicePolicyManager;
//import android.content.BroadcastReceiver;
import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
//import android.os.Bundle;
//import android.os.Environment;
import android.os.PowerManager;
//import android.os.SystemClock;
import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * Created by conan on 06/11/2017.
 */
//CONAN PASTE CLOSE

public class UserAreaActivity extends AppCompatActivity implements SensorEventListener {
    private FirebaseAuth auth;
    EditText editText;
    Button timerButton, stopButton, settings, soundly, btnDismiss, btnOpenPopup;
    TextView textView, tvGraph, ancor;
    LinearLayout linearLayoutTop;

    //CONAN PASTE OPEN
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

    private float lastTotal1 = 1000;
    private float lastTotal2 = 1000;
    private float lastTotal3 = 1000;

    private int sensitivity = 1;
    private double sleepThreshold = 0.15;

    private boolean saveFile;
    private boolean soundlyOn = false;

//    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

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
    //CONAN PASTE CLOSE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        //@Dan changed tvGraph, soundly and settings slightly
        //tvGraph = (TextView) findViewById(R.id.tvgraph);
        soundly = (Button) findViewById(R.id.bStart);
//        settings = (Button) findViewById(R.id.bSettings);
        timerButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
//        btnOpenPopup = (Button)findViewById(R.id.openpopup);
        linearLayoutTop = (LinearLayout) findViewById(R.id.linearLayoutTop);
        ancor = (TextView) findViewById(R.id.ancor);


//        tvGraph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserAreaActivity.this, SleepGraph.class));
//            }
//        });


        soundly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(UserAreaActivity.this, AccelerometerActivity.class));
                if(soundly.getText().equals("Start Soundly")){
                    Date date = new Date();
                    Context context = getApplicationContext();
                    createOutputFile(date.toString() + "\n");
                    soundlyOn = true;
                    soundly.setText("Stop Soundly");

                    CharSequence textT = "Soundly Activated";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, textT, duration);
                    toast.show();
                }
                else if(soundly.getText().equals("Stop Soundly")){
                    soundlyOn = false;
                    soundly.setText("Start Soundly");
                    Context context = getApplicationContext();
                    CharSequence textT = "Soundly Deactivated";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, textT, duration);
                    toast.show();
                    lastTotal1 = 1000;
                    lastTotal2 = 1000;
                    lastTotal3 = 1000;
                }

            }
        });
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UserAreaActivity.this, SettingsActivity.class));
//            }
//        });


        //creates onClickListener for start button.
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                //checks if there is user input; displays toast when no input has been given.
                if (editText.getText().toString().equals("")) {
                    Context context = getApplicationContext();
                    CharSequence textT = "Please enter time";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, textT, duration);
                    toast.show();
                }
                //if there is user input, checks if the value of the input is 0; displays toast prompting the user
                // for a number > 0 if true.
                else {
                    String text = editText.getText().toString();
                    int seconds = Integer.valueOf(text);

                    if (seconds == 0) {
                        Context context = getApplicationContext();
                        CharSequence textT = "Please enter valid time input";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, textT, duration);
                        toast.show();
                    }
                    //Once program has confirmed that there is a valid input put it creates a new CountDownTimer,
                    //sets up the progressBar and "freezes" the editText field.
                    else {
                        final CountDownTimer countDownTimer = new CountDownTimer(seconds * 60000, 1000) //60000//
                        {
                            @Override
                            public void onTick(long l) {
                                long minutes = l / 60000;
                                long seconds = l / 1000 % 60;
                                editText.setVisibility(View.GONE);
                                textView.setText("" + minutes + ":" + seconds);
                                textView.setVisibility(View.VISIBLE);

                            }

                            @Override
                            public void onFinish() {
                                Toast.makeText(UserAreaActivity.this, "Finished", Toast.LENGTH_LONG).show();
                                textView.setVisibility(View.GONE);
                                editText.setVisibility(View.VISIBLE);
                                editText.setFocusableInTouchMode(true);
                                System.out.println("finished");
//                                stopService(new Intent(getBaseContext(), TimerService.class));
                                forceMusicStop();
                            }
                        };

                        //starts timer
                        countDownTimer.start();
                        editText.setText("");
                        editText.setFocusable(false);
//                        startService(new Intent(getBaseContext(), TimerService.class));


                        //cancels timer on stopButton click
                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                countDownTimer.cancel();
                                textView.setVisibility(View.GONE);
                                editText.setVisibility(View.VISIBLE);
                                editText.setFocusableInTouchMode(true);
//                                stopService(new Intent(getBaseContext(), TimerService.class));
                            }
                        });
                    }
                }
            }
        });

        //CONAN PASTE OPEN
        // keeps running when screen is locked
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");


//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.accelo_example);
//        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            // fail! we don't have an accelerometer!
            Toast.makeText(this, "You need an accelerometer for soundly", Toast.LENGTH_SHORT).show();
        }


        startTime = System.currentTimeMillis();
        devicePolicyManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //CONAN PASTE CLOSE

    }

    //CONAN PASTE OPEN
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
            boolean deleted = yourFile.delete();

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

//    public void initializeViews() {
//        currentX = (TextView) findViewById(R.id.currentX);
//        currentY = (TextView) findViewById(R.id.currentY);
//        currentZ = (TextView) findViewById(R.id.currentZ);
//
//        maxX = (TextView) findViewById(R.id.maxX);
//        maxY = (TextView) findViewById(R.id.maxY);
//        maxZ = (TextView) findViewById(R.id.maxZ);
//    }

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
        if(soundlyOn) {
            long currentTime = System.currentTimeMillis();


            if (currentTime - startTime > 60000) {  //60000
//            forceMusicStop();
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

                if (lastTotal1 < sleepThreshold && lastTotal2 < sleepThreshold && lastTotal3 < sleepThreshold) {
                    // user is asleep
                    forceMusicStop();
                }

            }

            // clean current values
//        displayCleanValues();
            // display the current x,y,z accelerometer values
//        displayCurrentValues();
            // display the max x,y,z accelerometer values
            updateMaxValues();

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
    }

//    public void displayCleanValues() {
//        currentX.setText("0.0");
//        currentY.setText("0.0");
//        currentZ.setText("0.0");
//    }
//
//    // display the current x,y,z accelerometer values
//    public void displayCurrentValues() {
//        currentX.setText(Float.toString(deltaX));
//        currentY.setText(Float.toString(deltaY));
//        currentZ.setText(Float.toString(deltaZ));
//    }

    // display the max x,y,z accelerometer values
    public void updateMaxValues() {
        if (deltaX > deltaXMax) {
            deltaXMax = deltaX;
//            maxX.setText(Float.toString(deltaXMax));
        }
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
//            maxY.setText(Float.toString(deltaYMax));
        }
        if (deltaZ > deltaZMax) {
            deltaZMax = deltaZ;
//            maxZ.setText(Float.toString(deltaZMax));
        }
    }
    //CONAN PASTE CLOSE

    public void spotifyPlayPause(View view){
        try{
//        String uri = "spotifyPause:track:0IcSLT53eE07Jmok64Ppo3";
//        Intent launcher = new Intent( Intent.ACTION_VIEW, Uri.parse(uri) );
////        launcher.addFlags(Intent.FLAG_FROM_BACKGROUND);
//
//        Intent launcher = new Intent("com.spotify.mobile.android.ui.widget.PAUSE");
//
//        launcher.setPackage("com.spotify.music");
            int keyCode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
//
//            if (!mAudioManager.isMusicActive() && !isSpotifyRunning()) {
//            startMusicPlayer();
//            }

            Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
            i.setPackage("com.spotify.music");
            synchronized (this) {
                i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
                sendOrderedBroadcast(i, null);

                i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, keyCode));
                sendOrderedBroadcast(i, null);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Spotify not installed", Toast.LENGTH_LONG).show();
        }

    }

    public void spotifyNext(View view) {
        try {
            Intent launcher = new Intent("com.spotify.mobile.android.ui.widget.NEXT");

            launcher.setPackage("com.spotify.music");

            sendBroadcast(launcher);
        }
        catch (Exception e) {
            Toast.makeText(this, "Spotify not installed", Toast.LENGTH_LONG).show();
        }
    }

    public void openSpotify(View view) {
        try {
            String uri = "spotify:track:4TxfZzrq0dH2Wmfl4dAjN9";
            //https://open.spotify.com/track/4TxfZzrq0dH2Wmfl4dAjN9

            Intent launcher = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        launcher.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(launcher);
        } catch (Exception e) {
            Toast.makeText(this, "Spotify not installed", Toast.LENGTH_LONG).show();
        }
    }
    public void signOut() {
        auth.signOut();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void popUP(){
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.activity_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btnDismiss = (Button) popupView.findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(ancor, 60,0);
        popupWindow.setOutsideTouchable(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.menu_home:
//                intent = new Intent(this, UserAreaActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                break;
            case R.id.menu_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_sleep_graph:
                intent = new Intent(this, SleepGraph.class);
                startActivity(intent);
                break;
            case R.id.menu_sign_out:
                signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_about:
                popUP();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

}
