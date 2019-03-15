package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageActivity extends Activity {
    String number;
    private int SEND_MESSAGE_CODE = 1;
    String msg;
    private MessageTimerDatabase messageTimerDatabase;
    private MessageTimeTable messageTimeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageTimerDatabase = Room.databaseBuilder(getApplicationContext(), MessageTimerDatabase.class, "MessageTimerdb").allowMainThreadQueries().build();
        if(getIntent().getStringExtra("Timer") != null)
        {
            messageTimeTable = this.messageTimerDatabase.messageDao().getMessageTimerById(getIntent().getIntExtra("id",-1));
            if(messageTimeTable == null)
            {
                Toast.makeText(getApplicationContext(), "message timer is deleted", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //setContentView(R.layout.activity_timer);
        Log.d("messageText",getIntent().getStringExtra("messageText"));

        //searchContact();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            SendMessage();
        }else{
            requestSMSPermission();
        }
    }

    protected void SendMessage(){

        msg = getIntent().getStringExtra("messageText");
        number = getIntent().getStringExtra("number");
        if(number != null && msg != null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,msg,null,null);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void requestSMSPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS)){
            new AlertDialog.Builder(this)
                    .setTitle("permission is needed to Send sms")
                    .setMessage("To Send SMS and some funtionalities might not work properly")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_MESSAGE_CODE);
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
                SendMessage();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
