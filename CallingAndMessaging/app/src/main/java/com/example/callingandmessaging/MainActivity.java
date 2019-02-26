package com.example.callingandmessaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int READ_CONTACT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            ContactList contactList = (ContactList)getApplicationContext();
            new Thread(contactList).start();
        }else{
            requestReadContactPermission();
        }

    }

    public void setTimerByCall(View view) {
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Call");
        startActivity(intent);
    }

    public void setTimerByMessage(View view) {
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Message");
        startActivity(intent);
    }

    public void getVoice(View view) {
        Intent intent = new Intent(MainActivity.this,AudioRecorder.class);
        startActivity(intent);
    }

    protected void requestReadContactPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(this)
                    .setTitle("permission is needed to Read Contact")
                    .setMessage("some funtionalities might not work properly")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},READ_CONTACT_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},READ_CONTACT_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == READ_CONTACT_CODE){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                ContactList contactList = (ContactList)getApplicationContext();
                new Thread(contactList).start();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
