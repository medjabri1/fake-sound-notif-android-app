package com.example.fakesoundnotification;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {

    LinearLayout title_layout, developer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        title_layout = findViewById(R.id.title_layout);
        developer_layout = findViewById(R.id.developer_layout);

        playAnimations();

        final Intent intent = new Intent(this, MainActivity.class);

        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                    finish();
                }
            },
            3000
        );
    }

    private void playAnimations() {
        title_layout.setAlpha(0f);
        title_layout.setTranslationY(-200);
        title_layout.setScaleY(1.5f);
        title_layout.setScaleX(1.5f);

        developer_layout.setAlpha(0f);
        developer_layout.setTranslationY(50);

        title_layout.animate().alpha(1f).setDuration(500).setStartDelay(0);
        title_layout.animate().translationY(0).setDuration(500).setStartDelay(500);
        title_layout.animate().scaleY(1f).setDuration(500).setStartDelay(300);
        title_layout.animate().scaleX(1f).setDuration(500).setStartDelay(300);

        developer_layout.animate().alpha(1f).setDuration(500).setStartDelay(600);
        developer_layout.animate().translationY(0).setDuration(600).setStartDelay(600);
    }
}
