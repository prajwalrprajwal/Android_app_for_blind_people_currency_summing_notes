package com.example.bemyeyes;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.Locale;

/**TTSListener
 * Created by sonseongbin on 2017. 3. 19..
 */

public class TTS extends UtteranceProgressListener implements TextToSpeech.OnInitListener{

    private final String TAG = TTS.class.getSimpleName();

    private TextToSpeech textToSpeech;
    private Locale locale;
    private boolean textSpoken;
    private TTSListener ttsListener;


    public TTS(Context context, Locale locale) {
        this.locale = locale;
        textToSpeech = new TextToSpeech(context, this);
        textToSpeech.setOnUtteranceProgressListener(this);
    }

    public void setTTSListener(TTSListener listener) {
        this.ttsListener = listener;
    }

    public void speak(String text) {
        if(textToSpeech != null) {
            String myUtteranceID = "myUtteranceID";
            textToSpeech.setSpeechRate(0.8f); // Set the speech rate
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, myUtteranceID);
            textSpoken = true; // Set the flag to true when speaking starts

        }
    }

    public void stop() {
        textToSpeech.stop();
    }

    public void shutdown() {
        textToSpeech.shutdown();
    }

    public boolean isSpeaking() {
        return textToSpeech.isSpeaking();
    }

    @Override
    public void onStart(String utteranceId) {
        Log.d(TAG, "onStart / utteranceID = " + utteranceId);
    }

    @Override
    public void onDone(String utteranceId) {

        Log.d(TAG, "onDone / utteranceID = " + utteranceId);
        if (utteranceId.equals("myUtteranceID")) {
            textSpoken = false; // Set the flag to false when the utterance is completed
            if (ttsListener != null) {
                ttsListener.onTextSpoken(); // Notify the listener that the text has been spoken
            }
        }
    }

    @Override
    public void onError(String utteranceId) {
        Log.d(TAG, "onError / utteranceID = " + utteranceId);
    }

    @Override
    public void onInit(int status) {
        if(status != TextToSpeech.ERROR)
            textToSpeech.setLanguage(locale);
    }
    public interface TTSListener {
        void onTextSpoken();
    }

}
