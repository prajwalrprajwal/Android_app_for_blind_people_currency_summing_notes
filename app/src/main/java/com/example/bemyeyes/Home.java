package com.example.bemyeyes;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    // creating variables for view pager,
    // liner layout, adapter and our array list.
    private ViewPager viewPager;
    private LinearLayout dotsLL;
    com.example.bemyeyes.SliderAdapter adapter;
    private ArrayList<com.example.bemyeyes.SliderModal> sliderModalArrayList;
    private TextView[] dots;
    private GestureDetector gestureDetector;
    int size;
    Button skip;
    private TTS tts;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new TTS(this, Locale.ENGLISH);
        LinearLayout layout = findViewById(R.id.layout);

        gestureDetector = new GestureDetector(this, this);
        gestureDetector.setOnDoubleTapListener(this);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Pass the touch event to the GestureDetector
                gestureDetector.onTouchEvent(event);

                return true;
            }
        });


    }

    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent motionEvent) {

        tts.speak(" Welcome to NAYAN, an application designed to assist blind individuals, Please follow the instructions carefully to make the most of your experience.\n" +
                " Left Swipe (Currency Summing): To sum the currency notes, swipe left on the screen with your finger.\n" +
                " Right Swipe (Text-to-Speech Conversion): To convert text to speech, swipe right on the screen with your finger. This action will activate the text-to-speech feature,\n" +
                " Up Swipe (Currency Note Identification): To identify currency notes, swipe up on the screen with your finger. This action will activate the currency note identification feature.\n");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to be executed after 10 seconds
                Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                startActivity(new Intent(Home.this, MainActivity.class));
                vibe.vibrate(200);
                finish();
            }
        }, 40000);//40000
        Toast.makeText(Home.this, "Single click detected", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent motionEvent) {

        if (tts != null) {
            // Stop the TextToSpeech
            tts.stop();
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            startActivity(new Intent(Home.this, MainActivity.class));
            vibe.vibrate(200);
            finish();
            // Shutdown the TextToSpeech instance
        }
        Toast.makeText(Home.this, "Double click detected", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onStop() {
        super.onStop();

        // Check if the TextToSpeech instance is not null
        if (tts != null) {
            // Stop the TextToSpeech
            tts.stop();
            // Shutdown the TextToSpeech instance
        }
    }
}