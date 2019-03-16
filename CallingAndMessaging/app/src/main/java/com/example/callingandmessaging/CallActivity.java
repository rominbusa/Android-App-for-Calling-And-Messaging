package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class CallActivity extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private int CALL_CODE = 2;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("asda","asa");


        number = getIntent().getStringExtra("number");
        //searchContact();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            textToSpeech = new TextToSpeech(this,this);
        } else {
            requestCallPermission();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Bundle b = getIntent().getExtras();
        notificationManager.cancel(b.getInt("n_id"));
    }

    protected void makecall() {
        if(number != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(CallActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    protected void requestCallPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
            new AlertDialog.Builder(this)
                    .setTitle("permission is needed to Call")
                    .setMessage("Call funtionality might not work properly")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CallActivity.this,new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == CALL_CODE) {
            if(grantResults.length>0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                textToSpeech = new TextToSpeech(this,this);
            } else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS){
            int languageStatus = textToSpeech.setLanguage(Locale.ENGLISH);
            if(languageStatus == TextToSpeech.LANG_MISSING_DATA || languageStatus == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"sorry failsed due to Lang no supproted",Toast.LENGTH_SHORT).show();
            }else{
                String data = "Calling to  "+getIntent().getStringExtra("number");
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
                    Log.d("message","hey ther is me and mt ajda");
                }

                @Override
                public void onDone(String utteranceId) {
                    makecall();
                }

                @Override
                public void onError(String utteranceId) {

                }
            });


        }else{
            Toast.makeText(this,"sorry failsed due to some reason",Toast.LENGTH_SHORT).show();
        }
    }
}
