package com.example.littlepolaris;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StarsActivity extends AppCompatActivity {

    TextView txtStars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stars);

        txtStars = findViewById(R.id.txtStars);

        txtStars.setText(
                "Total Stars\n\n"
                        + StarManager.getStars(this)
        );
    }
}