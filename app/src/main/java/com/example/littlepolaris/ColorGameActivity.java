package com.example.littlepolaris;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class ColorGameActivity extends AppCompatActivity {

    ImageView imgColor;

    Button btn1, btn2, btn3;
    Button btnNext;
    Button btnBack;

    ImageButton btnAI;

    String correctColor;

    TextToSpeech tts;

    String[] colors = {
            "RED",
            "BLUE",
            "GREEN",
            "YELLOW",
            "ORANGE",
            "PINK",
            "PURPLE",
            "BLACK",
            "WHITE",
            "BROWN"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_game);

        imgColor = findViewById(R.id.imgColor);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnAI = findViewById(R.id.btnAI);

        btnBack.setOnClickListener(v -> finish());

        btnAI.setOnClickListener(v ->
                AIHelper.showAI(ColorGameActivity.this));

        tts = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                tts.setLanguage(Locale.US);

                tts.setSpeechRate(1.1f);

                tts.setPitch(1.2f);
            }
        });

        loadQuestion();

        btn1.setOnClickListener(v ->
                check(btn1.getText().toString()));

        btn2.setOnClickListener(v ->
                check(btn2.getText().toString()));

        btn3.setOnClickListener(v ->
                check(btn3.getText().toString()));

        btnNext.setOnClickListener(v ->
                loadQuestion());
    }

    private void loadQuestion() {

        Random r = new Random();

        correctColor = colors[r.nextInt(colors.length)];

        switch (correctColor) {

            case "RED":
                imgColor.setImageResource(R.drawable.red);
                break;

            case "BLUE":
                imgColor.setImageResource(R.drawable.blue);
                break;

            case "GREEN":
                imgColor.setImageResource(R.drawable.green);
                break;

            case "YELLOW":
                imgColor.setImageResource(R.drawable.yellow);
                break;

            case "ORANGE":
                imgColor.setImageResource(R.drawable.orange);
                break;

            case "PINK":
                imgColor.setImageResource(R.drawable.pink);
                break;

            case "PURPLE":
                imgColor.setImageResource(R.drawable.purple);
                break;

            case "BLACK":
                imgColor.setImageResource(R.drawable.black);
                break;

            case "WHITE":
                imgColor.setImageResource(R.drawable.white);
                break;

            case "BROWN":
                imgColor.setImageResource(R.drawable.brown);
                break;
        }

        ArrayList<String> options = new ArrayList<>();

        options.add(correctColor);

        while (options.size() < 3) {

            String color = colors[r.nextInt(colors.length)];

            if (!options.contains(color)) {
                options.add(color);
            }
        }

        Collections.shuffle(options);

        btn1.setText(options.get(0));
        btn2.setText(options.get(1));
        btn3.setText(options.get(2));

        if (tts != null) {

            tts.speak(
                    "Which color is this?",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "COLOR"
            );
        }
    }

    private void check(String selected) {

        if (selected.equals(correctColor)) {

            StarManager.addStars(this, 2);

            Toast.makeText(
                    this,
                    "⭐ Correct! +2 Stars",
                    Toast.LENGTH_SHORT
            ).show();

            imgColor.postDelayed(
                    this::loadQuestion,
                    800
            );

        } else {

            Toast.makeText(
                    this,
                    "❌ Try Again!",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }
}