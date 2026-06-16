package com.example.littlepolaris;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.media.MediaPlayer;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CardView cardLearn, cardGames, cardStars, cardParents;
    ImageButton btnAI,btnCMR;

    ImageView homeLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardLearn = findViewById(R.id.cardLearn);
        cardGames = findViewById(R.id.cardGames);
        cardStars = findViewById(R.id.cardStars);
        cardParents = findViewById(R.id.cardParents);

        btnAI = findViewById(R.id.btnAI);
        btnCMR = findViewById(R.id.btnCMR);

        homeLogo = findViewById(R.id.homeLogo);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        btnAI.startAnimation(bounce);
        btnCMR.startAnimation(bounce);

        cardLearn.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            MainActivity.this,
                            LearnActivity.class)), 200);
        });

        cardGames.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            MainActivity.this,
                            GamesActivity.class)), 200);
        });

        cardStars.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            MainActivity.this,
                            StarsActivity.class)), 200);
        });

        cardParents.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            MainActivity.this,
                            ParentsBoardActivity.class)), 200);
        });

        btnAI.setOnClickListener(v -> {

            cardBounce(v);

            v.postDelayed(() ->
                    AIHelper.showAI(MainActivity.this), 200);
        });

        btnCMR.setOnClickListener(v -> {

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            MainActivity.this,
                            LittleLens.class)), 200);
        });

        /* btnAI.setOnClickListener(v ->
                AIHelper.showAI(MainActivity.this));

        btnCMR.setOnClickListener(v ->
                startActivity(new Intent(this, LittleLens.class))); */

        ObjectAnimator floatLogo =
                ObjectAnimator.ofFloat(
                        homeLogo,
                        "translationY",
                        0f,
                        -25f,
                        0f);

        floatLogo.setDuration(2500);
        floatLogo.setRepeatCount(ValueAnimator.INFINITE);
        floatLogo.start();
    }

    private void playClickSound() {
        MediaPlayer mp =
                MediaPlayer.create(
                        this,
                        R.raw.click);

        mp.start();

        mp.setOnCompletionListener(
                MediaPlayer::release);
    }

    private void cardBounce(android.view.View view) {

        view.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .setDuration(150)
                .withEndAction(() ->
                        view.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(100));
    }

    // 🎤 MIC RESULT HANDLER
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AIHelper.handleVoiceResult(requestCode, resultCode, data);
    }
}