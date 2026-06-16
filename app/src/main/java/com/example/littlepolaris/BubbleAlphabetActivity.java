package com.example.littlepolaris;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BubbleAlphabetActivity extends AppCompatActivity {

    FrameLayout gameArea;
    TextView txtTarget;
    Button btnBack;

    char currentLetter = 'A';

    Random random = new Random();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_alphabet);

        gameArea = findViewById(R.id.gameArea);
        txtTarget = findViewById(R.id.txtTarget);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        gameArea.post(this::spawnBubbles);
    }

    private void spawnBubbles() {

        gameArea.removeAllViews();

        txtTarget.setText("Find " + currentLetter);

        ArrayList<Character> letters = new ArrayList<>();

        letters.add(currentLetter);

        while (letters.size() < 12) {

            char c = (char) ('A' + random.nextInt(26));

            if (!letters.contains(c)) {
                letters.add(c);
            }
        }

        Collections.shuffle(letters);

        for (Character c : letters) {
            createBubble(c);
        }
    }

    private void createBubble(char letter) {

        Button bubble = new Button(this);

        bubble.setText(String.valueOf(letter));

        bubble.setTextSize(20);

        bubble.setBackgroundResource(R.drawable.bubble_bg);

        bubble.setTextColor(Color.BLACK);

        int size = 130 + random.nextInt(90);

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(size, size);

        bubble.setLayoutParams(params);

        bubble.setRotation(
                random.nextInt(20) - 10
        );

        int maxX = Math.max(
                gameArea.getWidth() - size,
                1
        );

        int maxY = Math.max(
                gameArea.getHeight() - size,
                1
        );

        bubble.setX(random.nextInt(maxX));
        bubble.setY(random.nextInt(maxY));

        gameArea.addView(bubble);

        animateBubble(bubble);

        bubble.setOnClickListener(v -> {

            if (letter == currentLetter) {

                bubble.animate()
                        .scaleX(0)
                        .scaleY(0)
                        .setDuration(250)
                        .withEndAction(() -> {

                            currentLetter++;

                            StarManager.addStars(
                                    BubbleAlphabetActivity.this,
                                    1
                            );

                            if (currentLetter > 'Z') {

                                StarManager.addStars(
                                        BubbleAlphabetActivity.this,
                                        20
                                );

                                Toast.makeText(
                                        BubbleAlphabetActivity.this,
                                        "🏆 Alphabet Complete! +20 Stars",
                                        Toast.LENGTH_LONG
                                ).show();

                                currentLetter = 'A';
                            }

                            spawnBubbles();

                        })
                        .start();

            } else {

                bubble.animate()
                        .rotationBy(25)
                        .setDuration(100)
                        .withEndAction(() ->
                                bubble.animate()
                                        .rotationBy(-50)
                                        .setDuration(100)
                                        .withEndAction(() ->
                                                bubble.animate()
                                                        .rotationBy(25)
                                                        .setDuration(100)
                                        )
                        );

                bubble.setTextColor(Color.RED);

                handler.postDelayed(
                        () -> bubble.setTextColor(Color.BLACK),
                        400
                );
            }
        });
    }

    private void animateBubble(View bubble) {

        int maxX = Math.max(
                gameArea.getWidth() - bubble.getWidth(),
                1
        );

        int maxY = Math.max(
                gameArea.getHeight() - bubble.getHeight(),
                1
        );

        float newX = random.nextInt(maxX);
        float newY = random.nextInt(maxY);

        bubble.animate()
                .x(newX)
                .y(newY)
                .setDuration(
                        2500 + random.nextInt(2500)
                )
                .withEndAction(() -> {

                    if (!isFinishing()) {
                        animateBubble(bubble);
                    }

                })
                .start();
    }

    @Override
    protected void onDestroy() {

        handler.removeCallbacksAndMessages(null);

        super.onDestroy();
    }
}