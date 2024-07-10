package com.example.bemyeyes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.examples.classification.tflite.Classifier;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class DetectNotesToSpeech extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    Button camera, detect;
    ImageView imageView;
    int imageSize = 360;
    String DetectResult;
    Bitmap mBitmap;
    ProgressBar progressBar;
    final Handler handler = new Handler(Looper.getMainLooper());
    TextView text_view, text_view1;
    public Integer detectedNote=0, totalAmount=0;
    private static final float IMAGE_MEAN = 127.5f;
    private static final float IMAGE_STD = 127.5f;
    private GestureDetector gestureDetector;
    private long lastClickTime = 0;
    private static final int DOUBLE_CLICK_TIME_DELTA = 300; // Maximum duration between two clicks to consider as a double click
    private TTS tts;
    private org.tensorflow.lite.examples.classification.tflite.Classifier classifier;
    private static final org.tensorflow.lite.examples.classification.env.Logger LOGGER = new org.tensorflow.lite.examples.classification.env.Logger();



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_notes_to_speech);

        tts = new TTS(this, Locale.ENGLISH);
        RelativeLayout surface_view = findViewById(R.id.surface_view);
        text_view = findViewById(R.id.text_view);

        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);

//        surface_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(cameraIntent, 3);
//                } else {
//                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
//                }
//            }
//        });

//        surface_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Single click detected
//            }
//        });

        // Set an OnTouchListener to detect double clicks
        surface_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Pass the touch event to the GestureDetector
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        try {
            classifier = Classifier.create(this, Classifier.Model.FLOAT, Classifier.Device.CPU, 1);
        } catch (IOException e) {
            LOGGER.e(e, "Failed to create classifier.");
            Toast.makeText(this, "Failed to create classifier: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }



    public void classifyImage(Bitmap image){

        if (classifier != null) {
//                final long startTime = SystemClock.uptimeMillis();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, classifier.getImageSizeX(), classifier.getImageSizeY(), false);

            final List<Classifier.Recognition> results = classifier.recognizeImage(resizedBitmap);
            LOGGER.v("Detect results: %s", results);
            Classifier.Recognition recognition = results.get(0);
                text_view.setText(String.valueOf(recognition.getTitle()));
            Log.i("recognition.getTitle()", recognition.getTitle());
                tts.speak("detected Amount is " + String.valueOf(recognition.getTitle()) + "rupees");
        } else{
            Log.i("recognition.getTitle()", "classifier is null");

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                mBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("error1", String.valueOf(e));

                }

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                mBitmap = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, 3);
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(200);


            cameraIntent.putExtra("android.intent.extra.quickCapture", true);
            startActivityForResult(cameraIntent, 3);


        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }
        Toast.makeText(DetectNotesToSpeech.this, "Single click detected", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {
//        tts.speak("Total detected Amount is"+ String.valueOf(totalAmount) +"rupees");
//        Toast.makeText(DetectNotesToSpeech.this, "Double tap detected", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}