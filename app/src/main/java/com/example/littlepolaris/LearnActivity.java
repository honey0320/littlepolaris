package com.example.littlepolaris;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Button;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LearnActivity extends AppCompatActivity {


    CardView cardNumbers;
    CardView cardAlphabets;
    CardView cardColors;
    CardView cardShapes;
    CardView cardAnimals;
    CardView cardFruits;
    CardView cardVehicles;
    ImageButton btnAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        cardNumbers = findViewById(R.id.cardNumbers);
        cardAlphabets = findViewById(R.id.cardAlphabets);
        cardColors = findViewById(R.id.cardColors);
        cardShapes = findViewById(R.id.cardShapes);
        cardAnimals = findViewById(R.id.cardAnimals);
        cardFruits = findViewById(R.id.cardFruits);
        cardVehicles = findViewById(R.id.cardVehicles);

        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnAI = findViewById(R.id.btnAI);

        btnAI.setOnClickListener(v -> {

            cardBounce(v);

            v.postDelayed(() ->
                    AIHelper.showAI(LearnActivity.this), 200);
        });

        cardNumbers.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "NUMBERS");
                startActivity(i);
            }, 200);
        });

        cardAlphabets.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "ALPHABETS");
                startActivity(i);
            }, 200);
        });

        cardColors.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "COLORS");
                startActivity(i);
            }, 200);
        });

        cardShapes.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "SHAPES");
                startActivity(i);
            }, 200);
        });

        cardAnimals.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "ANIMALS");
                startActivity(i);
            }, 200);
        });

        cardFruits.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "FRUITS");
                startActivity(i);
            }, 200);
        });

        cardVehicles.setOnClickListener(v -> {

            playClickSound();

            cardBounce(v);

            v.postDelayed(() -> {
                Intent i = new Intent(this, FlashcardActivity.class);
                i.putExtra("CATEGORY", "VEHICLES");
                startActivity(i);
            }, 200);
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
