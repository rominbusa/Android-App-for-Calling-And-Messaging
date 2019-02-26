package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Log.d("messageText",getIntent().getStringExtra("messageText"));

        searchContact();

    }

    protected void searchContact(){
        String name = getIntent().getStringExtra("Selected_name");
        msg = getIntent().getStringExtra("messageText");
        if(name == null){
            return;
        }
        ContactList contactList = (ContactList)getApplicationContext();
        ArrayList<Person> personArr = contactList.getPerson();

        Log.d("name is ",name);
        for(int i = 0;i<personArr.size();i++){
            if(name.toLowerCase().contains(personArr.get(i).getName().toLowerCase())){
                number = personArr.get(i).getContact_no()[0];
                Log.d("your selected name is",number);
                break;
            }
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            SendMessage();
        }else{
            requestSMSPermission();
        }
    }

    protected void SendMessage(){
        if(number != null && msg != null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,msg,null,null);
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
