package com.example.littlepolaris;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.media.ExifInterface;
import android.graphics.Matrix;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.CameraSelector;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import android.util.Base64;

import androidx.camera.core.ImageCaptureException;

import android.graphics.Color;
import android.os.Handler;

import java.io.File;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.Locale;

public class LittleLens extends AppCompatActivity implements TextToSpeech.OnInitListener {

    // UI

    private PreviewView previewView;

    private ImageView capturedImage;

    private Button captureBtn;
    private Button hearBtn;
    private Button spellBtn;

    private TextView objectTitle;

    private LinearLayout resultPanel;

    // CameraX

    private ImageCapture imageCapture;

    // Speech

    private TextToSpeech tts;

    // Detected object

    private String detectedObject = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_little_lens);

        previewView = findViewById(R.id.previewView);

        capturedImage = findViewById(R.id.capturedImage);

        captureBtn = findViewById(R.id.captureBtn);

        hearBtn = findViewById(R.id.hearBtn);

        spellBtn = findViewById(R.id.spellBtn);

        objectTitle = findViewById(R.id.objectTitle);

        resultPanel = findViewById(R.id.resultPanel);

        tts = new TextToSpeech(this, this);

        previewView.setOnClickListener(v -> {

            if (resultPanel.getVisibility() == View.VISIBLE) {

                hideResultPanel();

            }

        });

        capturedImage.setVisibility(View.GONE);

        spellBtn.setOnClickListener(v -> {

            if (!detectedObject.isEmpty()) {

                spellWord(
                        detectedObject.toUpperCase()
                );

            }

        });

        requestCameraPermission();

        captureBtn.setOnClickListener(v -> {

            takePhoto();

        });

        hearBtn.setOnClickListener(v -> {

            if (!detectedObject.isEmpty()) {

                tts.speak(
                        detectedObject,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                );

            }

        });

    }

    private void requestCameraPermission() {

        ActivityResultLauncher<String> permissionLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.RequestPermission(),
                        isGranted -> {

                            if (isGranted) {

                                startCamera();

                            }

                        });

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED) {

            permissionLauncher.launch(
                    Manifest.permission.CAMERA
            );

        } else {

            startCamera();

        }

    }

    private String bitmapToBase64(Bitmap bitmap) {

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();

        bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                baos
        );

        byte[] imageBytes = baos.toByteArray();

        return android.util.Base64.encodeToString(
                imageBytes,
                android.util.Base64.NO_WRAP
        );
    }

    private void startCamera() {

        ListenableFuture<ProcessCameraProvider>
                cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {

            try {

                ProcessCameraProvider cameraProvider =
                        cameraProviderFuture.get();

                Preview preview =
                        new Preview.Builder()
                                .build();

                preview.setSurfaceProvider(
                        previewView.getSurfaceProvider()
                );

                imageCapture =
                        new ImageCapture.Builder()
                                .build();

                CameraSelector cameraSelector =
                        CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();

                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture
                );

            } catch (Exception e) {

                e.printStackTrace();

            }

        }, ContextCompat.getMainExecutor(this));

    }

    private void takePhoto() {

        if (imageCapture == null)
            return;

        File photoFile =
                new File(
                        getExternalCacheDir(),
                        "captured.jpg"
                );

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(
                        photoFile
                ).build();

        imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),

                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onImageSaved(
                            @NonNull
                            ImageCapture.OutputFileResults
                                    outputFileResults) {

                        Bitmap bitmap =
                                BitmapFactory.decodeFile(
                                        photoFile.getAbsolutePath()
                                );

                        try {

                            ExifInterface exif =
                                    new ExifInterface(
                                            photoFile.getAbsolutePath()
                                    );

                            int orientation =
                                    exif.getAttributeInt(
                                            ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_NORMAL
                                    );

                            Matrix matrix = new Matrix();

                            if (orientation ==
                                    ExifInterface.ORIENTATION_ROTATE_90) {

                                matrix.postRotate(90);

                            } else if (orientation ==
                                    ExifInterface.ORIENTATION_ROTATE_180) {

                                matrix.postRotate(180);

                            } else if (orientation ==
                                    ExifInterface.ORIENTATION_ROTATE_270) {

                                matrix.postRotate(270);

                            }

                            bitmap = Bitmap.createBitmap(
                                    bitmap,
                                    0,
                                    0,
                                    bitmap.getWidth(),
                                    bitmap.getHeight(),
                                    matrix,
                                    true
                            );

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                        capturedImage.setImageBitmap(
                                bitmap
                        );

                        animateResultPanel();

                        detectObjectAI(bitmap);

                    }

                    @Override
                    public void onError(
                            @NonNull
                            ImageCaptureException exception) {

                        exception.printStackTrace();

                    }

                });

    }

    private void animateResultPanel() {

        capturedImage.setVisibility(View.VISIBLE);

        resultPanel.setVisibility(View.VISIBLE);

        resultPanel.setTranslationY(500f);

        resultPanel.animate()
                .translationY(0)
                .setDuration(600)
                .setInterpolator(
                        new AccelerateDecelerateInterpolator()
                )
                .start();

        captureBtn.animate()
                .translationY(-280f)
                .setDuration(600)
                .setInterpolator(
                        new AccelerateDecelerateInterpolator()
                )
                .start();
    }

    private void hideResultPanel() {

        resultPanel.animate()
                .translationY(resultPanel.getHeight())
                .setDuration(300)
                .withEndAction(() -> {

                    resultPanel.setVisibility(View.GONE);
                    capturedImage.setVisibility(View.GONE);

                })
                .start();

        captureBtn.animate()
                .translationY(0)
                .setDuration(300)
                .start();
    }

    private void detectObjectAI(Bitmap bitmap) {

        objectTitle.setText("Thinking...");

        String base64Image =
                bitmapToBase64(bitmap);

        AIHelper.identifyImage(
                base64Image,
                new AIHelper.AICallback() {

                    @Override
                    public void onSuccess(String result) {

                        runOnUiThread(() -> {

                            detectedObject = result
                                    .replace("\n", "")
                                    .replace(".", "")
                                    .trim();

                            objectTitle.setText(
                                    detectedObject.toUpperCase()
                            );

                        });

                    }

                    @Override
                    public void onError(String error) {

                        runOnUiThread(() -> {

                            detectedObject = "UNKNOWN";

                            objectTitle.setText(
                                    "UNKNOWN"
                            );

                        });

                    }

                }
        );

    }

    private void spellWord(String word) {

        Handler handler = new Handler();

        for (int i = 0; i < word.length(); i++) {

            int index = i;

            handler.postDelayed(() -> {

                SpannableString span =
                        new SpannableString(word);

                span.setSpan(
                        new RelativeSizeSpan(2.2f),
                        index,
                        index + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                span.setSpan(
                        new ForegroundColorSpan(
                                Color.RED
                        ),
                        index,
                        index + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                objectTitle.setText(span);

                String letter =
                        String.valueOf(
                                word.charAt(index)
                        );

                tts.speak(
                        letter,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                );

                if(index == word.length()-1){

                    handler.postDelayed(() -> {

                        objectTitle.setText(word);

                    },800);

                }

            }, i * 1200);

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

    @Override
    public void onBackPressed() {

        if (resultPanel.getVisibility() == View.VISIBLE) {

            hideResultPanel();
            return;

        }

        super.onBackPressed();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            tts.setLanguage(Locale.US);

        }

    }

}