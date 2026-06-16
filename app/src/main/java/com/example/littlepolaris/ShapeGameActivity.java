package com.example.littlepolaris;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class ShapeGameActivity extends AppCompatActivity {

    TextView txtTargetShape;

    ImageView img1, img2, img3;

    Button btnNext;
    Button btnBack;

    ImageButton btnAI;

    String correctShape;

    TextToSpeech tts;

    String[] shapes = {
            "CIRCLE",
            "SQUARE",
            "TRIANGLE",
            "RECTANGLE",
            "STAR",
            "OVAL",
            "DIAMOND"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_game);

        txtTargetShape = findViewById(R.id.txtTargetShape);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);

        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);

        btnAI = findViewById(R.id.btnAI);

        btnBack.setOnClickListener(v ->
                finish());

        btnAI.setOnClickListener(v ->
                AIHelper.showAI(ShapeGameActivity.this));

        tts = new TextToSpeech(this, status -> {

            if (status == TextToSpeech.SUCCESS) {

                tts.setLanguage(Locale.US);

                tts.setSpeechRate(1.3f);

                tts.setPitch(1.2f);
            }
        });

        loadQuestion();

        img1.setOnClickListener(v ->
                check("CIRCLE"));

        img2.setOnClickListener(v ->
                check("SQUARE"));

        img3.setOnClickListener(v ->
                check("TRIANGLE"));

        btnNext.setOnClickListener(v ->
                loadQuestion());
    }

    private void loadQuestion() {

        Random r = new Random();

        int index = r.nextInt(shapes.length);

        correctShape = shapes[index];

        txtTargetShape.setText(correctShape);

        if (tts != null) {

            tts.speak(
                    "Find the shape " + correctShape,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "SHAPE"
            );
        }
    }

    private void check(String selected) {

        if (selected.equals(correctShape)) {

            StarManager.addStars(this, 2);

            Toast.makeText(
                    this,
                    "⭐ Correct! +2 Stars",
                    Toast.LENGTH_SHORT
            ).show();

            txtTargetShape.postDelayed(
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