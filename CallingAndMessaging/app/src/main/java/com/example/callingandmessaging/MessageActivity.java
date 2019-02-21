package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageActivity extends Activity {
    String number;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Log.d("messageText",getIntent().getStringExtra("messageText"));
        searchContact();
    }

    protected void searchContact(){
        Bundle b = getIntent().getExtras();

//        String name= b.getString("Selected_name"); //getIntent().getStringExtra("name");
        String name=getIntent().getStringExtra("Selected_name");
        msg=getIntent().getStringExtra("messageText");
        if(name==null){
            return;
        }
        Log.d("dasjahgsd","sdadadada");

        ContactList contactList = (ContactList)getApplicationContext();
        ArrayList<Person> personArr = contactList.getPerson();

        Log.d("name is ",name);
        for(int i=0;i<personArr.size();i++){
            if(name.toLowerCase().equals((String) personArr.get(i).getName().toLowerCase())){
                number=(String)personArr.get(i).getContact_no()[0];
                Log.d("your selected name is",number);
                break;
            }
        }

        SendMessage();
    }

    protected void SendMessage(){
        if(number!=null && msg!=null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,msg,null,null);
        }
    }


}
