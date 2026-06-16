package com.example.littlepolaris;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class GamesActivity extends AppCompatActivity {

    CardView cardNumbersGame, cardAlphabetGame, cardColorGame, cardShapeGame, cardBubbleAlphabet;
    Button btnBack;
    ImageButton btnAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        cardNumbersGame = findViewById(R.id.cardNumbersGame);
        cardAlphabetGame = findViewById(R.id.cardAlphabetGame);
        cardColorGame = findViewById(R.id.cardColorGame);
        cardShapeGame = findViewById(R.id.cardShapeGame);
        cardBubbleAlphabet = findViewById(R.id.cardBubbleAlphabet);

        btnBack = findViewById(R.id.btnBack);
        btnAI = findViewById(R.id.btnAI);

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        btnAI.setOnClickListener(v -> {

            cardBounce(v);

            v.postDelayed(() ->
                    AIHelper.showAI(GamesActivity.this), 200);
        });

// Number Game
        cardNumbersGame.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            GamesActivity.this,
                            NumberGameActivity.class)), 200);
        });

// Alphabet Game
        cardAlphabetGame.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            GamesActivity.this,
                            AlphabetGameActivity.class)), 200);
        });

// Color Game
        cardColorGame.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            GamesActivity.this,
                            ColorGameActivity.class)), 200);
        });

// Shape Game
        cardShapeGame.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            GamesActivity.this,
                            ShapeGameActivity.class)), 200);
        });
// Bubble Game
        cardBubbleAlphabet.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() ->
                    startActivity(new Intent(
                            GamesActivity.this,
                            BubbleAlphabetActivity.class)), 200);
        });
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
}