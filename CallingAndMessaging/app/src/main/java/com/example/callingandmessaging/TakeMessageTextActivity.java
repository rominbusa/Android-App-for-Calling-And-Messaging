package com.example.callingandmessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class TakeMessageTextActivity extends Activity {

    String text_of_audio;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        record_audio();
    }

    public void record_audio(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 10:

                if(resultCode == RESULT_OK && data != null ){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_of_audio=result.get(0);

                    Bundle b = getIntent().getExtras();

                    Intent intent = new Intent(this,MessageActivity.class);
                    intent.putExtra("messageText",text_of_audio);
                    intent.putExtra("number",b.getString("number"));

                    startActivity(intent);
                }
                break;
        }

    }

}
