package com.example.soundly;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;
import android.app.Service;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class TimerService extends Service {

    AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            //
        }
    };

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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        forceMusicStop();
    }

}