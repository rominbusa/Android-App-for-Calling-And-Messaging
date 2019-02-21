package com.example.callingandmessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AudioRecorder extends Activity {
    String text_of_audio,audioStr;

    BuildURL URLBuilder = new BuildURL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record_audio();
    }

    //record audio by google api
    public void record_audio() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent,10);

    }

    //audio to text by response of google api and call build url
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:

                if(resultCode==RESULT_OK && data!=null ){
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_of_audio=result.get(0);

                    audioStr=URLBuilder.buildUrl(text_of_audio);
                    if(audioStr.contains("call")){
                        audioStr = audioStr.substring(4);
                        Log.d("name",audioStr);
                        Intent intent = new Intent(this,CallActivity.class);
                        intent.putExtra("name",audioStr);
                        startActivity(intent);
                    }
                    Log.d("audio",audioStr);
                }
                break;
        }

    }
}