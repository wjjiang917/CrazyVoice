package com.crazyjiang.crazyvoice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private AudioManager audioManager;

    ImageView closeIv;
    SeekBar mediaBar;
    SeekBar callBar;
    SeekBar ringBar;
    SeekBar systemBar;
    SeekBar alarmBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        closeIv = findViewById(R.id.iv_close);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mediaBar = findViewById(R.id.media_bar);
        callBar = findViewById(R.id.call_bar);
        ringBar = findViewById(R.id.ring_bar);
        systemBar = findViewById(R.id.system_bar);
        alarmBar = findViewById(R.id.alarm_bar);

        // finish when touch
        setFinishOnTouchOutside(true);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // call voice
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        Log.d("VOICE", "call voice, max: " + max + ", current: " + current);
        callBar.setMax(max);
        callBar.setProgress(current);

        // system
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Log.d("VOICE", "system voice, max: " + max + ", current: " + current);
        systemBar.setMax(max);
        systemBar.setProgress(current);

        // ring
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        Log.d("VOICE", "ring voice, max: " + max + ", current: " + current);
        ringBar.setMax(max);
        ringBar.setProgress(current);

        // music
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("VOICE", "music voice, max: " + max + ", current: " + current);
        mediaBar.setMax(max);
        mediaBar.setProgress(current);

        // alarm
        max = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
        Log.d("VOICE", "alarm voice, max: " + max + ", current: " + current);
        alarmBar.setMax(max);
        alarmBar.setProgress(current);

        mediaBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(AudioManager.STREAM_MUSIC));
        callBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(AudioManager.STREAM_VOICE_CALL));
        systemBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(AudioManager.STREAM_SYSTEM));
        ringBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(AudioManager.STREAM_RING));
        alarmBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(AudioManager.STREAM_ALARM));

        // addToStatusBar();
    }

    class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private int streamType;

        public OnSeekBarChangeListener(int streamType) {
            this.streamType = streamType;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            audioManager.setStreamVolume(streamType, progress, AudioManager.FLAG_PLAY_SOUND);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
//                audioManager.adjustStreamVolume(
//                        AudioManager.STREAM_MUSIC,
//                        AudioManager.ADJUST_RAISE,
//                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
//                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
//                audioManager.adjustStreamVolume(
//                        AudioManager.STREAM_MUSIC,
//                        AudioManager.ADJUST_LOWER,
//                        AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();

        showNotification();
    }

    private void showNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getIntent(), 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new Notification();
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
//        notification.flags |= Notification.FLAG_NO_CLEAR;
//        notification.contentIntent = pendingIntent;

        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setWhen(System.currentTimeMillis())
                .setTicker("Ticker")
                .setContentTitle("Title")
                .setContentText("Text")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();

        notificationManager.notify(0, notification);
    }
}
