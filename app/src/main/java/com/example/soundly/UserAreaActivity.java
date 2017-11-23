package com.example.soundly;

import android.content.Intent;
import android.net.Uri;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        TextView tvGraph = (TextView) findViewById(R.id.tvgraph);

        Button timer = (Button) findViewById(R.id.bTimer);

        Button soundly = (Button) findViewById(R.id.bStart);



        tvGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, SleepGraph.class));
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, TimerActivity.class));
            }
        });

        soundly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAreaActivity.this, AccelerometerActivity.class));
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
