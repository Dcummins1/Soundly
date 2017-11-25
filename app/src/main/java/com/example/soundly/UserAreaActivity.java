package com.example.soundly;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class UserAreaActivity extends AppCompatActivity {

    EditText editText;
    //    ProgressBar progressBar;
    Button timerButton, stopButton, settings, soundly;
    //timerButton was previously startButton
    TextView textView, tvGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        //@Dan changed tvGraph, soundly and settings slightly
        tvGraph = (TextView) findViewById(R.id.tvgraph);
        soundly = (Button) findViewById(R.id.bStart);
        settings = (Button) findViewById(R.id.bSettings);
        timerButton = (Button) findViewById(R.id.startButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);


        tvGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, SleepGraph.class));
            }
        });


        soundly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, AccelerometerActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, SettingsActivity.class));
            }
        });


        //creates onClickListener for start button.
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checks if there is user input; displays toast when no input has been given.
                if (editText.getText().toString().equals("")) {
                    Context context = getApplicationContext();
                    CharSequence textT = "Please enter time";
                    int duration = Toast.LENGTH_LONG;

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
                        int duration = Toast.LENGTH_LONG;

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
                                textView.setText("Time remaining: " + minutes + ":" + seconds);
                            }

                            @Override
                            public void onFinish() {
                                textView.setText("Finished!");
                                editText.setFocusableInTouchMode(true);
                                System.out.println("finished");
                                stopService(new Intent(getBaseContext(), TimerService.class));
                            }
                        };

                        //starts timer
                        countDownTimer.start();
                        editText.setText("");
                        editText.setFocusable(false);
                        startService(new Intent(getBaseContext(), TimerService.class));

                        //cancels timer on stopButton click
                        stopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                countDownTimer.cancel();
                                textView.setText("");
                                editText.setFocusableInTouchMode(true);
                                stopService(new Intent(getBaseContext(), TimerService.class));
                            }
                        });
                    }
                }
            }
        });

    }

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
}
