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

public class NumberGameActivity extends AppCompatActivity {

    TextView txtTarget;

    Button btn1, btn2, btn3;
    Button btnNext;
    Button btnBack;

    ImageButton btnAI;

    int correctAnswer;

    TextToSpeech tts;

    String[] numberWords = {
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_game);

        txtTarget = findViewById(R.id.txtTarget);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnAI = findViewById(R.id.btnAI);

        tts = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                tts.setLanguage(Locale.US);

                tts.setSpeechRate(1.1f);

                tts.setPitch(1.1f);

                int result = tts.setLanguage(Locale.US);

                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    Toast.makeText(
                            this,
                            "Text To Speech Not Supported",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        loadQuestion();

        btn1.setOnClickListener(v ->
                checkAnswer(Integer.parseInt(btn1.getText().toString())));

        btn2.setOnClickListener(v ->
                checkAnswer(Integer.parseInt(btn2.getText().toString())));

        btn3.setOnClickListener(v ->
                checkAnswer(Integer.parseInt(btn3.getText().toString())));

        btnNext.setOnClickListener(v ->
                loadQuestion());

        btnBack.setOnClickListener(v ->
                finish());

        btnAI.setOnClickListener(v ->
                AIHelper.showAI(NumberGameActivity.this));
    }

    private void loadQuestion() {

        Random r = new Random();

        correctAnswer = r.nextInt(10) + 1;

        txtTarget.setText(numberWords[correctAnswer - 1]);

        ArrayList<Integer> options = new ArrayList<>();
        options.add(correctAnswer);

        while (options.size() < 3) {

            int num = r.nextInt(10) + 1;

            if (!options.contains(num)) {
                options.add(num);
            }
        }

        Collections.shuffle(options);

        btn1.setText(String.valueOf(options.get(0)));
        btn2.setText(String.valueOf(options.get(1)));
        btn3.setText(String.valueOf(options.get(2)));

        if (tts != null) {

            tts.speak(
                    "Can you find number " +
                            numberWords[correctAnswer - 1],
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "NUMBER"
            );
        }
    }

    private void checkAnswer(int selected) {

        if (selected == correctAnswer) {

            StarManager.addStars(this, 2);

            Toast.makeText(
                    this,
                    "⭐ Correct! +2 Stars",
                    Toast.LENGTH_SHORT
            ).show();

            txtTarget.postDelayed(
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