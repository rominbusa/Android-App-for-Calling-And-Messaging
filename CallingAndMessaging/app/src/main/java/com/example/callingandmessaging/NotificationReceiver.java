package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationReceiver extends Activity {

    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("yeah","yeah it works..");
       // Toast.makeText(NotificationReceiver.this,"NotificationReceiverActivity",Toast.LENGTH_SHORT);
        searchContact();
    }

    protected void searchContact(){
        Bundle b = getIntent().getExtras();

//        String name= b.getString("Selected_name"); //getIntent().getStringExtra("name");
        String name=getIntent().getStringExtra("Selected_name");
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

        makecall();
    }

    protected void makecall(){
        if(number!=null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(NotificationReceiver.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }
}
