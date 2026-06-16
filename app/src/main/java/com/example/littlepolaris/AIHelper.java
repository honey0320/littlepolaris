package com.example.littlepolaris;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AIHelper {

    private static final String API_KEY =
            "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";

    private static final String API_URL =
            "https://openrouter.ai/api/v1/chat/completions";

    private static final OkHttpClient client = new OkHttpClient();

    // used to store dialog refs for mic result
    private static EditText currentInput;

    public static void showAI(Activity activity) {

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.ai_chat_popup);
        dialog.setCancelable(true);
        dialog.show();

        TextView btnClose = dialog.findViewById(R.id.btnClose);
        EditText edtQuestion = dialog.findViewById(R.id.edtQuestion);
        Button btnAsk = dialog.findViewById(R.id.btnAsk);
        ImageButton btnMic = dialog.findViewById(R.id.btnMic);
        TextView txtResponse = dialog.findViewById(R.id.txtResponse);

        currentInput = edtQuestion;

        // 🔊 TTS
        final TextToSpeech[] tts = new TextToSpeech[1];
        tts[0] = new TextToSpeech(activity, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts[0].setLanguage(Locale.US);
            }
        });

        btnClose.setOnClickListener(v -> {

            if (tts[0] != null) {
                tts[0].stop();
                tts[0].shutdown();
            }

            dialog.dismiss();
        });

        // 🎤 MIC INPUT
        btnMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

            activity.startActivityForResult(intent, 1001);
        });

        // 💬 ASK AI
        btnAsk.setOnClickListener(v -> {

            String question = edtQuestion.getText().toString().trim();

            if (question.isEmpty()) {
                txtResponse.setText("Please type something 😊");
                return;
            }

            txtResponse.setText("Thinking... 🤖");

            getGeminiResponse(question, new AIResponseCallback() {
                @Override
                public void onSuccess(String response) {

                    activity.runOnUiThread(() -> {
                        txtResponse.setText(response);

                        tts[0].speak(
                                response,
                                TextToSpeech.QUEUE_FLUSH,
                                null,
                                null
                        );
                    });
                }

                @Override
                public void onError(String error) {
                    activity.runOnUiThread(() ->
                            txtResponse.setText("Error: " + error)
                    );
                }
            });
        });
    }

    // 🎤 RESULT FROM MIC (CALL THIS IN YOUR ACTIVITY)
    public static void handleVoiceResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK && data != null) {

            ArrayList<String> result =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (result != null && !result.isEmpty() && currentInput != null) {
                currentInput.setText(result.get(0));
            }
        }
    }

    // 🔥 GEMINI CALL
    private static void getGeminiResponse(String question,
                                          AIResponseCallback callback) {

        try {

            JSONObject bodyJson = new JSONObject();

            bodyJson.put(
                    "model",
                    "deepseek/deepseek-chat-v3-0324"
            );

            JSONArray messages = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put(
                    "content",
                    "You help small children learn. "
                            + "Always answer in short sentence. "
                            + "Use easy words a 5-year-old can understand. "
                            + "Be friendly and cheerful. "
                            + "Never use hard words or long explanations."
            );

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);

            messages.put(systemMessage);
            messages.put(userMessage);

            bodyJson.put("messages", messages);

            RequestBody body = RequestBody.create(
                    bodyJson.toString(),
                    MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader(
                            "Authorization",
                            "Bearer " + API_KEY
                    )
                    .addHeader(
                            "Content-Type",
                            "application/json"
                    )
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call,
                                      IOException e) {

                    callback.onError(
                            e.getMessage()
                    );
                }

                @Override
                public void onResponse(Call call,
                                       Response response)
                        throws IOException {

                    if (response.body() == null) {

                        callback.onError(
                                "Empty response"
                        );

                        return;
                    }

                    String res =
                            response.body().string();

                    try {

                        JSONObject obj =
                                new JSONObject(res);

                        if (!obj.has("choices")) {

                            callback.onError(
                                    "API Error:\n" + res
                            );

                            return;
                        }

                        String answer =
                                obj.getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content");

                        callback.onSuccess(answer);

                    } catch (Exception e) {

                        callback.onError(
                                "Parse Error: "
                                        + e.getMessage()
                        );
                    }
                }
            });

        } catch (Exception e) {

            callback.onError(
                    e.getMessage()
            );
        }
    }

    public static void identifyImage(
            String base64Image,
            AICallback callback
    ) {

        try {

            JSONObject root =
                    new JSONObject();

            root.put(
                    "model",
                    "qwen/qwen2.5-vl-72b-instruct"
            );
            root.put("max_tokens", 20);

            JSONArray messages =
                    new JSONArray();

            JSONObject user =
                    new JSONObject();

            user.put("role","user");

            JSONArray content =
                    new JSONArray();

            JSONObject text =
                    new JSONObject();

            text.put(
                    "type",
                    "text"
            );

            text.put(
                    "text",
                    "Look carefully at this image. Identify the single main object. Reply with only the object name, for example: Monkey, Bottle, Mouse, Cat, Chair"
            );

            JSONObject image =
                    new JSONObject();

            image.put(
                    "type",
                    "image_url"
            );

            JSONObject imageUrl =
                    new JSONObject();

            imageUrl.put(
                    "url",
                    "data:image/jpeg;base64," + base64Image
            );

            image.put(
                    "image_url",
                    imageUrl
            );

            content.put(text);
            content.put(image);

            user.put(
                    "content",
                    content
            );

            messages.put(user);

            root.put(
                    "messages",
                    messages
            );

            RequestBody body =
                    RequestBody.create(
                            root.toString(),
                            MediaType.get("application/json")
                    );

            Request request =
                    new Request.Builder()
                            .url(
                                    "https://openrouter.ai/api/v1/chat/completions"
                            )
                            .addHeader(
                                    "Authorization",
                                    "Bearer " + API_KEY
                            )
                            .addHeader(
                                    "Content-Type",
                                    "application/json"
                            )
                            .post(body)
                            .build();

            client.newCall(request)
                    .enqueue(new Callback() {

                        @Override
                        public void onFailure(
                                Call call,
                                IOException e
                        ) {

                            callback.onError(
                                    e.getMessage()
                            );

                        }

                        @Override
                        public void onResponse(
                                Call call,
                                Response response
                        ) throws IOException {

                            String json =
                                    response.body()
                                            .string();

                            try {

                                JSONObject obj =
                                        new JSONObject(json);

                                String answer =
                                        obj.getJSONArray(
                                                        "choices")
                                                .getJSONObject(0)
                                                .getJSONObject(
                                                        "message")
                                                .getString(
                                                        "content");

                                callback.onSuccess(
                                        answer
                                );

                            } catch (Exception e) {

                                callback.onError(
                                        e.getMessage()
                                );

                            }

                        }

                    });

        } catch (Exception e) {

            callback.onError(
                    e.getMessage()
            );

        }

    }

    interface AICallback {
        void onSuccess(String response);
        void onError(String error);
    }

    interface AIResponseCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}