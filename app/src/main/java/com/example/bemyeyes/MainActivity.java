package com.example.bemyeyes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RelativeLayout layout;
    private TTS tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = findViewById(R.id.relativeLayout);
        tts = new TTS(this, Locale.ENGLISH);
        TextView text = findViewById(R.id.textview1);
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        text.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("","Swipe left to detect Currency notes");
//                tts.speak("Swipe left to detect Currency notes");
//
//                tts.speak("Swipe left to detect Currency note\n Swipe right to detect text");
//            }
//        });

        layout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

            public void onClick() {
                vibe.vibrate(200);
                tts.speak("Swipe left to detect Currency notes");

                tts.speak("Swipe right to detect text");
            }
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                tts.speak(" Do Single tap to open the camera and start counting, Do the Double tap hear the counted amount");
                vibe.vibrate(200);

                startActivity(new Intent(MainActivity.this, DetectNotes.class));

                Toast.makeText(MainActivity.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                tts.speak(" Put the camera in front of the text");
                vibe.vibrate(200);

                startActivity(new Intent(MainActivity.this, TextToSpeech.class));
                Toast.makeText(MainActivity.this, "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                tts.speak(" Do single tap to open the camera and start detecting");
                vibe.vibrate(200);

                startActivity(new Intent(MainActivity.this, DetectNotesToSpeech.class));
                Toast.makeText(MainActivity.this, "Swipe Up gesture detected", Toast.LENGTH_SHORT).show();
            }
        });
    }

}