package com.example.nitai.client_nitai;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.saeid.fabloading.LoadingView;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    public static MainActivity context;
    public static MessagesListFragmant messagesList;
    public static AudioManager audioManager;
    public static ButtonFragment buttonFragment;
    public static ArrayAdapter<CharSequence> adapter;
    public static SpeechRecognizer mSpeechRecognizer;
    public static Intent mSpeechRecognizerIntent;
    public static Map<String, WikiObject> wikiMap;
    public static BlockingQueue<Pair<String, WikiObject>> wikiMapQueue;
    public static Queue<Pair<String, WikiObject>> wikiMapQueue2;

    public static BlockingQueue<String> textRecognizedQueue;
    public static RequestQueue queue;
    public static ObjectPopAsyncTask asyncTask;
    public static Thread phrasesThread;
    public static String userLanguage = "en";
    public static LoadingView mLoadingView;
    public static AVLoadingIndicatorView mListeningView1;
    public static WikiFragment wikiFragment;
    public static Map<String, String> languageMap;


    public static void setUserLanguage(String userLanguage) {
        MainActivity.userLanguage = userLanguage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        checkPermission();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        queue = Volley.newRequestQueue(this);
        wikiMap = new HashMap<>();
        wikiMapQueue = new LinkedBlockingQueue<>();
        wikiMapQueue2 = new LinkedList<>();
        textRecognizedQueue = new LinkedBlockingQueue<>();
        buttonFragment = new ButtonFragment();
        messagesList = new MessagesListFragmant();
        wikiFragment = new WikiFragment();
        phrasesThread = new Thread(new PhrasesThread());
        languageMap();
        asyncTask = new ObjectPopAsyncTask();
        adapter = ArrayAdapter.createFromResource(this, R.array.planets_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmantViewHolder, buttonFragment, "buttonFragment");
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().findFragmentByTag("buttonFragment").isVisible()) {
            MainActivity.unSilence();
            phrasesThread.interrupt();
            asyncTask.cancel(true);
            mSpeechRecognizer.destroy();
        } else if (getSupportFragmentManager().findFragmentByTag("messageListFragment").isVisible()) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
        unSilence();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
        phrasesThread.interrupt();
        unSilence();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    200);
        }
    }

    public static void backClick() {
        wikiFragment.getFragmentManager().popBackStack();
    }

    public void startAnim() {
        mListeningView1.smoothToShow();
        // or avi.show();
    }

    public void stopAnim() {
        mListeningView1.smoothToHide();
        //mListeningView1.hide();
    }

    @SuppressLint("InlinedApi")
    public static void silence() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
    }

    @SuppressLint("InlinedApi")
    public static void unSilence() {
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
    }

    public static void setSpeechRecognizer() {
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        mSpeechRecognizer.setRecognitionListener(new recoListener());
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    public static void startThreads() {
        phrasesThread = new Thread(new PhrasesThread());
        asyncTask = new ObjectPopAsyncTask();
        phrasesThread.start();
        asyncTask.execute(wikiMapQueue);
    }

    public static void addLoadingView() {
        mLoadingView.addAnimation(Color.parseColor("#C7E7FB"), R.mipmap.catchapp1, LoadingView.FROM_LEFT);
        mLoadingView.addAnimation(Color.parseColor("#25c7cc"), R.mipmap.catchapp2, LoadingView.FROM_TOP);
        mLoadingView.addAnimation(Color.parseColor("#FF4218"), R.mipmap.catchapp1, LoadingView.FROM_RIGHT);
        mLoadingView.addAnimation(Color.parseColor("#FFD200"), R.mipmap.catchapp3, LoadingView.FROM_BOTTOM);
    }

    private void languageMap() {
        languageMap = new HashMap<>();
        languageMap.put("English", "en");
        languageMap.put("Hebrew", "he");
        languageMap.put("Spanish", "es");
        languageMap.put("German", "de");
        languageMap.put("Russian", "ru");
        languageMap.put("French", "fr");
        languageMap.put("Chinese", "zh");
    }
}
