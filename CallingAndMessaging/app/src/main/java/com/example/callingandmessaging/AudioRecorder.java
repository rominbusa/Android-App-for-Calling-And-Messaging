package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AudioRecorder extends Activity {
    String text_of_audio=null, audioStr=null, number=null, msg, name=null;

    BuildURL URLBuilder = new BuildURL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record_audio();
    }

    //record audio by google api
    public void record_audio() {


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        startActivityForResult(intent, 10);

    }


    //audio to text by response of google api and call build url
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:

                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text_of_audio = result.get(0);

                    audioStr = URLBuilder.buildUrl(text_of_audio);
                    if (audioStr.contains("call")) {
                        audioStr = audioStr.substring(4);
                        Log.d("name", audioStr);
                        name = audioStr;
                        searchContact();
                        Intent intent = new Intent(this, CallActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("number", number);
                        if(number != null) {
                            startActivity(intent);
                        }else {
                            record_audio();
                        }
                    } else if (audioStr.contains("message")) {
                        audioStr = audioStr.substring(7);
                        Log.d("name", audioStr);
                        name = audioStr;
                        searchContact();
                        Intent intent = new Intent(this, TakeMessageTextActivity.class);
                        intent.putExtra("number", number);
                        intent.putExtra("name",name);
                        if(number != null) {
                            startActivity(intent);
                        }else {
                            record_audio();
                        }
                    }else{
                        record_audio();
                    }
                    Log.d("audio", audioStr);
                }
                break;
        }

    }

    protected void searchContact() {
        if (name == null) {
            return;

        }
        ContactList contactList = (ContactList) getApplicationContext();
        ArrayList<Person> personArr = contactList.getPerson();

        Log.d("name is ", name);
        for (int i = 0; i < personArr.size(); i++) {
            Log.d("matched",personArr.get(i).getName());
            if (personArr.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                number = personArr.get(i).getContact_no()[0];
                Log.d("your selected name is", number);
                break;
            }
        }

    }
}
