package com.example.nitai.client_nitai;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import io.saeid.fabloading.LoadingView;


public class ButtonFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_button, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton mLoadingView = view.findViewById(R.id.loading_view);
        mLoadingView.setOnClickListener(recognitionButtonListener);
        MainActivity.mLoadingView = view.findViewById(R.id.loading_view);
        MainActivity.addLoadingView();
//        Spinner spinner = view.findViewById(R.id.userLang);
//        spinner.setAdapter(MainActivity.adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view,
//                                       int position, long id) {
//                Object item = adapterView.getItemAtPosition(position);
//                if (!item.toString().equals(MainActivity.userLanguage)) {
//                    MainActivity.setUserLanguage(MainActivity.languageMap.get(item.toString()));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        MainActivity.mLoadingView.addListener(new LoadingView.LoadingListener() {
            @Override
            public void onAnimationStart(int currentItemPosition) {
                Log.d("", "onAnimationRepeat: ");

            }
            @Override
            public void onAnimationRepeat(int nextItemPosition) {
                //MainActivity.mLoadingView.resumeAnimation();
                Log.d("", "onAnimationRepeat: ");


            }
            @Override
            public void onAnimationEnd(int nextItemPosition) {
                MainActivity.mLoadingView.startAnimation();

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.mLoadingView.pauseAnimation();

    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.mLoadingView.startAnimation();

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.mLoadingView.resumeAnimation();
    }

    View.OnClickListener recognitionButtonListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            onRecognitionButtonClicked();
        }
    };

    public void onRecognitionButtonClicked() {
        MainActivity.setSpeechRecognizer();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmantViewHolder, MainActivity.messagesList, "messageListFragment");
        transaction.addToBackStack(null);
        transaction.commit();
        if ((MainActivity.phrasesThread.getState().name().equals("NEW") || MainActivity.phrasesThread.getState().name().equals("TERMINATED")) && (MainActivity.asyncTask.getStatus().name().equals("PENDING") || MainActivity.asyncTask.getStatus().name().equals("FINISHED"))) {
            MainActivity.startThreads();
        }
        //MainActivity.silence();
        //phrasesThread.join();
        //wikiThread.join();
    }
}

