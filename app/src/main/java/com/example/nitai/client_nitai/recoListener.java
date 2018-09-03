package com.example.nitai.client_nitai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

@SuppressLint("Registered")
public class recoListener extends MainActivity implements RecognitionListener {


    @Override
    public void onResults(Bundle bundle) {
        final ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        try {
            if (matches != null) {
                MainActivity.textRecognizedQueue.put(matches.get(0));
                Log.i("recoListener", "matches: " + matches.get(0));

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPartialResults(Bundle bundle) {
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        MainActivity.silence();
        Log.i("", "onBeginningOfSpeech: ");
        startAnim();
//        visualization(this);
//        MainActivity.audioVisualization.onResume();
    }

    @Override
    public void onBeginningOfSpeech() {
        MainActivity.silence();
    }

    @Override
    public void onRmsChanged(float v) {
    }

    @Override
    public void onBufferReceived(byte[] bytes) {
    }

    @Override
    public void onEndOfSpeech() {
        stopAnim();
    }

    @Override
    public void onError(int i) {
        stopAnim();
        Log.i("recoListener", "error: " + i);
        mSpeechRecognizer.destroy();
        mSpeechRecognizer.setRecognitionListener(new recoListener());
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {
    }


}
