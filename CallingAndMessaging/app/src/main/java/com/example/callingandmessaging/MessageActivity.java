package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
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

public class MessageActivity extends BroadcastReceiver {
    String number;
    String msg;
    private MessageTimerDatabase messageTimerDatabase;
    private MessageTimeTable messageTimeTable;
    private Intent mintent;
    private Context mcontext;

    private TimerActivity timerActivity;
    @Override
    public void onReceive(Context context, Intent intent) {

        mintent=intent;
        mcontext=context;
//        {
//        super.onCreate(savedInstanceState);

        messageTimerDatabase = Room.databaseBuilder(context, MessageTimerDatabase.class, "MessageTimerdb").allowMainThreadQueries().build();
        if(intent.getStringExtra("Timer") != null)
        {
            messageTimeTable = this.messageTimerDatabase.messageDao().getMessageTimerById(intent.getIntExtra("id",-1));
            if(messageTimeTable == null)
            {
                Toast.makeText(context, "message timer is deleted", Toast.LENGTH_SHORT).show();
                return;
            }

            messageTimerDatabase.close();
        }

        Log.d("messageText",intent.getStringExtra("messageText"));
        SendMessage();
    }


    protected void SendMessage(){

        msg = mintent.getStringExtra("messageText");
        number = mintent.getStringExtra("number");
        if(number != null && msg != null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,msg,null,null);
        }
        Intent intent = new Intent(mcontext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        mcontext.startActivity(intent);
    }


}
