package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class TakeMessageTextActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    String text_of_audio;
    private int SEND_MESSAGE_CODE = 1;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        textToSpeech = new TextToSpeech(this,this);
//        record_audio();
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



                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        //write code if you want to show any message like permission already granted
                        toMessageActivity();
                    }else{
                        requestSMSPermission();
                    }

                }
                break;
        }

    }

    public void toMessageActivity(){
        Bundle b = getIntent().getExtras();
        Intent intent = new Intent(this,MessageActivity.class);
        intent.putExtra("messageText",text_of_audio);
        intent.putExtra("number",b.getString("number"));
        sendBroadcast(intent);
//        startActivity(intent);
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int languageStatus = textToSpeech.setLanguage(Locale.ENGLISH);
            if(languageStatus == TextToSpeech.LANG_MISSING_DATA || languageStatus == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"sorry failsed due to Lang no supproted",Toast.LENGTH_SHORT).show();
            }else{
                String data = "speak message to "+getIntent().getStringExtra("number");
                int stext = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    stext = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH,null,"uttid");
                }

                if(stext == TextToSpeech.ERROR){
                    Toast.makeText(this,"sorry man",Toast.LENGTH_SHORT).show();

                }

            }

            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    Log.d("message","just about to start speaking");
                }

                @Override
                public void onDone(String utteranceId) {

                    record_audio();
                }

                @Override
                public void onError(String utteranceId) {

                }
            });


        }else{
            Toast.makeText(this,"sorry failsed due to some reason",Toast.LENGTH_SHORT).show();
        }
    }


    protected void requestSMSPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
            new AlertDialog.Builder(this)
                    .setTitle("permission is needed to Send sms")
                    .setMessage("To Send SMS and some funtionalities might not work properly")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(TakeMessageTextActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_MESSAGE_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},SEND_MESSAGE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == SEND_MESSAGE_CODE){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                toMessageActivity();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
