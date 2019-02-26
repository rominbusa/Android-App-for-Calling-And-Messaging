package com.example.callingandmessaging;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telecom.Call;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

public class CallActivity extends Activity {

    private int CALL_CODE=2;
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
        if(name == null){
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

        

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            makecall();
        }else{
            requestCallPermission();
        }
    }

    protected void makecall(){
        if(number!=null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            if (ActivityCompat.checkSelfPermission(CallActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);
        }
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
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == CALL_CODE){
            if(grantResults.length>0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makecall();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
