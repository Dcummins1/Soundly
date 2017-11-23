//package com.example.soundly;
//
//import android.content.Context;
//import android.view.View;
//
///**
// * Created by Daniel on 23/11/2017.
// */
//
//
//public class Spotify implements View.OnClickListener{
//    private static Context context;
//
//    public static void setContext(Context context){
//        Spotify.context = context;
//    }
//    public static void spotifyNext(View view) {
//        Intent launcher = new Intent("com.spotify.mobile.android.ui.widget.NEXT");
//
//        launcher.setPackage("com.spotify.music");
//
//        context.sendBroadcast(launcher);
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId())
//        { case nextButton
//            //handle multiple view click events
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
////    public void nextSong(View view) {
////            int keyCode = KeyEvent.KEYCODE_MEDIA_NEXT;
////
////            if (!isSpotifyRunning()) {
////            startMusicPlayer();
////            }
////
////            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
////            intent.setPackage("com.spotify.music");
////    synchronized (this) {
////            intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
////            sendOrderedBroadcast(intent, null);
////
////            intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, keyCode));
////            getContext().sendOrderedBroadcast(intent, null);
////            }
////            }
////
//
////    public void playPauseMusic() {
////            int keyCode = KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE;
////
////            if (!mAudioManager.isMusicActive() && !isSpotifyRunning()) {
////            startMusicPlayer();
////            }
////
////            Intent i = new Intent(Intent.ACTION_MEDIA_BUTTON);
////            i.setPackage("com.spotify.music");
////    synchronized (this) {
////            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
////            getContext().sendOrderedBroadcast(i, null);
////
////            i.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, keyCode));
////            getContext().sendOrderedBroadcast(i, null);
////            }
////            }
////
////    private void startMusicPlayer() {
////            Intent startPlayer = new Intent(Intent.ACTION_MAIN);
////            startPlayer.setPackage("com.spotify.music");
////            startPlayer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            getContext().startActivity(startPlayer);
////
////            if (mMusicPlayerStartTimer != null) {
////            mMusicPlayerStartTimer.cancel();
////            }
////
////            mMusicPlayerStartTimer = new Timer("MusicPlayerStartTimer", true);
////            mMusicPlayerStartTimer.schedule(new MusicPlayerStartTimerTask(), DateUtils.SECOND_IN_MILLIS, DateUtils.SECOND_IN_MILLIS);
////            }
////
////    private boolean isSpotifyRunning() {
////            Process ps = null;
////            try {
////            String[] cmd = {
////            "sh",
////            "-c",
////            "ps | grep com.spotify.music"
////            };
////
////            ps = Runtime.getRuntime().exec(cmd);
////            ps.waitFor();
////
////            return ps.exitValue() == 0;
////            } catch (IOException e) {
////            Log.e(DEBUG_TAG, "Could not execute ps", e);
////            } catch (InterruptedException e) {
////            Log.e(DEBUG_TAG, "Could not execute ps", e);
////            } finally {
////            if (ps != null) {
////            ps.destroy();
////            }
////            }
////
////            return false;
////            }
////
////    private class MusicPlayerStartTimerTask extends TimerTask {
////        @Override
////        public void run() {
////            if (isSpotifyRunning()) {
////                playPauseMusic(null);
////                cancel();
////            }
////        }
////    }
////}
//}
