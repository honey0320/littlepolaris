package com.example.littlepolaris;

import android.content.Context;
import android.content.SharedPreferences;

public class StarManager {

    private static final String PREF_NAME = "LittlePolaris";
    private static final String KEY_STARS = "stars";

    public static void addStars(Context context, int stars){

        SharedPreferences sp =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE);

        int currentStars =
                sp.getInt(KEY_STARS,0);

        sp.edit()
                .putInt(
                        KEY_STARS,
                        currentStars + stars
                )
                .apply();
    }

    public static int getStars(Context context){

        SharedPreferences sp =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE);

        return sp.getInt(KEY_STARS,0);
    }
}