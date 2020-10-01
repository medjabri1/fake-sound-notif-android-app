package com.example.fakesoundnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private boolean isPlaying = false;

    private Button main_button;
    private LinearLayout main_button_parent;

    private TextView repeat_amount, delay_amount;
    private SeekBar repeat_amount_seekbar, delay_amount_seekbar;

    private Spinner sound_spinner;
    private String[] soundNames = {"Facebook Messenger", "Samsung Galaxy", "IOS Notification", "Iphone Ding", "Snapchat Snap"};

    private ValueAnimator animator;
    Timer timer;

    private SoundPool soundPool;
    private int[] sounds = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_button = findViewById(R.id.main_button);
        main_button_parent = findViewById(R.id.main_button_parent);

        repeat_amount = findViewById(R.id.repeat_amount);
        repeat_amount_seekbar = findViewById(R.id.repeat_amount_seekbar);

        //Init sounds
        loadSounds();

        //Sound Spinner
        sound_spinner = findViewById(R.id.sound_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item_layout, R.id.spinner_textview, soundNames);
        sound_spinner.setAdapter(arrayAdapter);

        //SeekBars
        delay_amount = findViewById(R.id.delay_amount);
        delay_amount_seekbar = findViewById(R.id.delay_amount_seekbar);

        repeat_amount_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                repeat_amount.setText(String.valueOf(repeat_amount_seekbar.getProgress()));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        delay_amount_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                delay_amount.setText(String.valueOf(delay_amount_seekbar.getProgress()));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    private void loadSounds() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        }

        int[] sounds_id = new int[5];

        sounds_id[0] = R.raw.facebook_messenger;
        sounds_id[1] = R.raw.samsung_galaxy;
        sounds_id[2] = R.raw.ios_notification;
        sounds_id[3] = R.raw.iphone_ding;
        sounds_id[4] = R.raw.snapchat_snap;

        for(int i = 0; i < sounds_id.length; i++) {
            sounds[i] = soundPool.load(MainActivity.this, sounds_id[i], 1);
        }
    }

    @SuppressLint("SetTextI18n")
    public void mainButtonClick(View view) {

        if(!isPlaying) {
            Toast.makeText(MainActivity.this, "Starting...", Toast.LENGTH_SHORT).show();
            main_button.setText("Stop");
            playSounds();
            animateMainButton();
            isPlaying = !isPlaying;
        } else {
            stopSounds();
        }
    }

    @SuppressLint("SetTextI18n")
    private void stopSounds() {
        isPlaying = !isPlaying;
        Toast.makeText(MainActivity.this, "Stopping...", Toast.LENGTH_SHORT).show();
        main_button.setText("Start");
        animator.pause();
        main_button_parent.setPadding(0, 0, 0, 0);
        main_button_parent.setAlpha(1);
        timer.cancel();
    }

    private void playSounds() {
        final int[] repeat_nbr = {repeat_amount_seekbar.getProgress()};
        int delay_time = (int) Math.max(500, delay_amount_seekbar.getProgress() * 1000);
        final String sound_name = sound_spinner.getSelectedItem().toString();

        timer = new Timer();

        final Handler mHandler = new Handler(Looper.getMainLooper());

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (repeat_nbr[0] <= 0) {
                            stopSounds();
                        } else {
                            playSound(sound_name);
                            repeat_nbr[0]--;
                        }
                    }
                });

            }
        };

        timer.scheduleAtFixedRate(timerTask, delay_time, delay_time);
    }

    public void playSound(String sound_name) {
        int sound_index = Arrays.asList(soundNames).indexOf(sound_name);
        soundPool.play(sounds[sound_index], 1, 1, 0, 0, 1);
    }

    public void animateMainButton() {
        animator = ValueAnimator.ofFloat(0f, 70f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float)animation.getAnimatedValue();
                float alphaAnimValue = animatedValue / 30;
                alphaAnimValue = Math.max(alphaAnimValue, 0.5f);
                main_button_parent.setPadding((int) animatedValue, (int) animatedValue, (int) animatedValue, (int) animatedValue);
                main_button_parent.setAlpha(alphaAnimValue);
            }
        });
    }

}
