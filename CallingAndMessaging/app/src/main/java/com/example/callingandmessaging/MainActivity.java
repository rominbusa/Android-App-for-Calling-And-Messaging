package com.example.callingandmessaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeeMessageTimerFragment.OnFragmentInteractionListener, SeeCallTimerFragment.OnFragmentInteractionListener  {

    private int READ_CONTACT_CODE = 1;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private int SEND_MESSAGE_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewPager for Tabs
        viewPager = findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //checking permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            //write code if you want to show any message like permission already granted
            ContactList contactList = (ContactList) getApplicationContext();
            ArrayList<Person> personArrayList = contactList.getPerson();
            if(personArrayList.isEmpty()) {
                new Thread(contactList).start();
            }
        } else {
            requestReadContactPermission();
        }

        //for bottom navigation bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.voiceButton:
                        // do something here
                        getVoice(item.getActionView());
                        return true;
                    case R.id.callByTimer:
                        // do something here
                        setTimerByCall(item.getActionView());
                        return true;
                    case R.id.messageByTimer:
                        // do something here
                        setTimerByMessage(item.getActionView());
                        return true;
                    case R.id.userGuide:
                        //do something here
                        Log.d("userGuide","userguide");
                        userManual();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewPager.setAdapter(adapter);
    }

    public void setTimerByCall(View view) {
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Call");
        startActivity(intent);
    }

    public void setTimerByMessage(View view) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            showContactMessageTimer();
        }else{
            requestSMSPermission();
        }
    }

    public void showContactMessageTimer(){
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Message");
        startActivity(intent);
    }
    public void getVoice(View view) {
        Intent intent = new Intent(MainActivity.this,AudioRecorder.class);
        startActivity(intent);
    }

    public void userManual(){
        Intent intent = new Intent(MainActivity.this,userManual.class);
        startActivity(intent);
    }

    protected void requestReadContactPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
            new AlertDialog.Builder(this)
                    .setTitle("permission is needed to Read Contact")
                    .setMessage("some functionalities might not work properly")
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

        if(requestCode == SEND_MESSAGE_CODE){
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showContactMessageTimer();
            }else{
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

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
                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_MESSAGE_CODE);
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



}
