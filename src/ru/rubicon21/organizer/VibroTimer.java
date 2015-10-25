package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by roman on 24.10.2015.
 */
public class VibroTimer extends Activity {

    private Timer vtTimer;
    private VibroTask vtTask;
    private final long taskPeriod = 30;
    private final long incPeriod = 250;
    private ProgressBar pbTimer;
    private Button btnStop;

    final String LOG_TAG = "myLogs";
    final String DATA_SD = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
            + "/music.mp3";

    MediaPlayer mediaPlayer;
    AudioManager am;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vibro_timer);

        am = (AudioManager) getSystemService(AUDIO_SERVICE);

        pbTimer = (ProgressBar) findViewById(R.id.pbTimer);
        btnStop = (Button) findViewById(R.id.btnStop);

        vtTimer = new Timer();
        vtTask = new VibroTask();

        vtTimer.schedule(vtTask, incPeriod, incPeriod);

        View.OnClickListener OnClickButtonStopTimer = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vtTimer.cancel();
                VibroTimer.this.finish();
            }
        };
        btnStop.setOnClickListener(OnClickButtonStopTimer);
    }

    @Override
    protected void onStop() {
        super.onStop();
        vtTimer.cancel();
    }

    class VibroTask extends TimerTask {
        @Override
        public void run() {
            releaseMP();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            long milliseconds = 500;

            if(pbTimer.getProgress() == pbTimer.getMax()){
                pbTimer.setProgress(0);
                v.vibrate(milliseconds);
                Log.d(LOG_TAG, "vibro");

                //try {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.balloon_pop);
                    mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        releaseMP();
                    }
                });
//                    throw new IOException();


                //}catch (IOException e){
                //    e.printStackTrace();
               // }

            }
            pbTimer.incrementProgressBy(1);
        }
    }

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMP();
    }
}