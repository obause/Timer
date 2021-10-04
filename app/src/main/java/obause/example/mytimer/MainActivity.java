package obause.example.mytimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    final static int MAXTIME = 60;
    final static int MINTIME = 1;
    final static int STARTTIME = 300;
    int time;
    boolean counterActive = false;
    Button startButton;

    TextView timerTextView;
    SeekBar timerSeekBar;
    CountDownTimer countDownTimer;
    Switch playSoundSwitch;

    public void resetTimer() {
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();

        counterActive = false;
        startButton.setText("Start");
    }

    public void buttonClicked(View view) {

        if (counterActive) {
            //Timer zurücksetzen
            resetTimer();
        }
        else {

            counterActive = true;
            timerSeekBar.setEnabled(false);
            startButton.setText("Stop");

            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long l) {
                    updateTimer((int)l/1000);
                }

                @Override
                public void onFinish() {
                    Log.i("Timer", "Zeit abgelaufen");

                    if (playSoundSwitch.isActivated()) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.timer_sound);
                        mediaPlayer.start();
                    }
                    resetTimer();
                }
            }.start();
        }
    }

    public void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String sSeconds = Integer.toString(seconds);
        if (sSeconds.length() == 1) {
            sSeconds = "0" + sSeconds;
        }

        timerTextView.setText(Integer.toString(minutes) + ":" + sSeconds);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playSoundSwitch = findViewById(R.id.playSoundSwitch);
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        timerSeekBar = findViewById(R.id.timerSeekBar);

        timerSeekBar.setMax(MAXTIME);
        timerSeekBar.setProgress(STARTTIME);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    timerSeekBar.setMin(MINTIME);
                    time = i;
                }
                else if (i < MINTIME) {
                    time = MINTIME;
                    timerSeekBar.setProgress(MINTIME);
                }
                else {
                    time = i;
                }

                updateTimer(time);

                Log.i("Seekbar Wert", Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}