package com.example.littlepolaris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logo = findViewById(R.id.logo);

        // Start animation
        Animation zoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        logo.startAnimation(zoom);

        // Optional startup sound
        //mediaPlayer = MediaPlayer.create(this, R.raw.start_sound);
        //mediaPlayer.start();

        // Move to MainActivity after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(
                        SplashActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}