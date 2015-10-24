package ru.rubicon21.organizer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vibro_timer);

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

    class VibroTask extends TimerTask {
        @Override
        public void run() {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            long milliseconds = 500;

            if(pbTimer.getProgress() == pbTimer.getMax()){
                pbTimer.setProgress(0);
                //v.vibrate(milliseconds);
            }
            pbTimer.incrementProgressBy(1);
        }
    }
}