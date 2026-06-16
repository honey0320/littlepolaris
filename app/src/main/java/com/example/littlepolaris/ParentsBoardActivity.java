package com.example.littlepolaris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ParentsBoardActivity extends AppCompatActivity {

    TextView txtStars, txtProgress;

    Button btnBack;
    ImageButton btnAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_board);

        txtStars = findViewById(R.id.txtStars);
        txtProgress = findViewById(R.id.txtProgress);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v ->
                finish());

        SharedPreferences sp =
                getSharedPreferences("LittlePolaris", MODE_PRIVATE);

        int stars = sp.getInt("stars", 0);

        txtStars.setText("Stars Earned: " + stars);

        StringBuilder progress = new StringBuilder();

        progress.append("🔢 Numbers: ")
                .append(getStatus(sp, "NUMBERS_DONE"))
                .append("\n\n");

        progress.append("🔤 Alphabets: ")
                .append(getStatus(sp, "ALPHABETS_DONE"))
                .append("\n\n");

        progress.append("🎨 Colors: ")
                .append(getStatus(sp, "COLORS_DONE"))
                .append("\n\n");

        progress.append("🔺 Shapes: ")
                .append(getStatus(sp, "SHAPES_DONE"))
                .append("\n\n");

        progress.append("🐶 Animals: ")
                .append(getStatus(sp, "ANIMALS_DONE"))
                .append("\n\n");

        progress.append("🍎 Fruits: ")
                .append(getStatus(sp, "FRUITS_DONE"))
                .append("\n\n");

        progress.append("🚗 Vehicles: ")
                .append(getStatus(sp, "VEHICLES_DONE"));

        txtProgress.setText(progress.toString());
    }

    private String getStatus(SharedPreferences sp, String key) {

        boolean done = sp.getBoolean(key, false);

        return done
                ? "✅ Completed"
                : "❌ Not Completed";
    }
}