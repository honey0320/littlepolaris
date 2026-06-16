package com.example.littlepolaris;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class AlphabetGameActivity extends AppCompatActivity {

    TextView txtPattern;

    Button btn1, btn2, btn3, btnNext;

    ImageButton btnAI, btnBack;

    char missingLetter;

    TextToSpeech tts;

    char[] letters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet_game);

        txtPattern = findViewById(R.id.txtPattern);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        btnNext = findViewById(R.id.btnNext);

        btnAI = findViewById(R.id.btnAI);

        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        tts = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                tts.setLanguage(Locale.US);

                tts.setSpeechRate(1.3f);

                tts.setPitch(1.1f);
            }
        });

        loadQuestion();

        btn1.setOnClickListener(v ->
                check(btn1.getText().toString().charAt(0)));

        btn2.setOnClickListener(v ->
                check(btn2.getText().toString().charAt(0)));

        btn3.setOnClickListener(v ->
                check(btn3.getText().toString().charAt(0)));

        btnNext.setOnClickListener(v ->
                loadQuestion());

        btnBack.setOnClickListener(v ->
                finish());

        btnAI.setOnClickListener(v ->
                AIHelper.showAI(
                        AlphabetGameActivity.this));
    }



    void loadQuestion() {

        Random r = new Random();

        int index = r.nextInt(24) + 1;

        char first = letters[index - 1];
        missingLetter = letters[index];
        char third = letters[index + 1];

        txtPattern.setText(
                first + " _ " + third);

        ArrayList<Character> options =
                new ArrayList<>();

        options.add(missingLetter);

        while (options.size() < 3) {

            char c =
                    letters[r.nextInt(26)];

            if (!options.contains(c)) {
                options.add(c);
            }
        }

        Collections.shuffle(options);

        btn1.setText(
                String.valueOf(options.get(0)));

        btn2.setText(
                String.valueOf(options.get(1)));

        btn3.setText(
                String.valueOf(options.get(2)));

        if (tts != null) {

            tts.stop();

            tts.speak(
                    "Find the missing letter",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "LETTER"
            );
        }
    }

    void check(char selected) {

        if (selected == missingLetter) {

            StarManager.addStars(this, 2);

            Toast.makeText(
                    this,
                    "⭐ Correct! +2 Stars",
                    Toast.LENGTH_SHORT
            ).show();

            txtPattern.postDelayed(
                    this::loadQuestion,
                    1000
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