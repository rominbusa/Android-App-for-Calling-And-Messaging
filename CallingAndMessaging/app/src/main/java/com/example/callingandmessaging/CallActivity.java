package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class CallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContactList contactList = (ContactList)getApplicationContext();
        ArrayList<Person> contact = contactList.getPerson();

        Iterator itr = contact.iterator();
        Person person=null;
        try {
            while (itr.hasNext()) {
                person = (Person) itr.next();
                Log.d("trying to get ame", person.getName());
                if (person.getName().toLowerCase().contains((getIntent().getExtras().getString("name").toLowerCase()))) {
                    Log.d("finally got it", person.getName());
                    break;
                }

            }

            Log.d("calling to ", person.getName());
        }catch (NullPointerException e){

        }
        String arr[]=person.getContact_no();

        if(arr[0]!=null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + arr[0]));
//            stringBuilderQuryResult.append(contactnumbers[0][0]);
//            showContact.setText(stringBuilderQuryResult);
            if (ActivityCompat.checkSelfPermission(CallActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
    }
}
