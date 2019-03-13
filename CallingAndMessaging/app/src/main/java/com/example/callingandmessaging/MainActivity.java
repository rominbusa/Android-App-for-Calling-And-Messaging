package com.example.callingandmessaging;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SeeCallTimerFragment.OnFragmentInteractionListener, SeeMessageTimerFragment.OnFragmentInteractionListener {

    private int READ_CONTACT_CODE = 1;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
            //write code if you want to show any message like permission already granted
            ContactList contactList = (ContactList)getApplicationContext();
            new Thread(contactList).start();
        }else{
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
                        actionBar.setTitle("Speak");
                        getVoice(item.getActionView());
                        return true;
                    case R.id.callByTimer:
                        // do something here
                        actionBar.setTitle("Schedule Call");
                        setTimerByCall(item.getActionView());
                        return true;
                    case R.id.messageByTimer:
                        // do something here
                        actionBar.setTitle("Schedule Message");
                        setTimerByMessage(item.getActionView());
                        return true;
                    case R.id.userGuide:
                        //do something here
                        actionBar.setTitle("User Manual");
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
        Fragment fragment = new SeeCallTimerFragment();
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment)
    {
        //load the fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    public void seeCallTimers(View view)
    {
        Fragment fragment = new SeeCallTimerFragment();
        loadFragment(fragment);
//        Intent intent = new Intent(MainActivity.this, DisplayCallTimers.class);
//        startActivity(intent);
    }

    public void seeMessageTimers(View view)
    {
        Fragment fragment = new SeeMessageTimerFragment();
        loadFragment(fragment);
//        Intent intent = new Intent(MainActivity.this, DisplayMessageTimers.class);q
//        startActivity(intent);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
