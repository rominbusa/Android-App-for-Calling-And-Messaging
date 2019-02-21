package com.example.callingandmessaging;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContactList contactList = (ContactList)getApplicationContext();
        new Thread(contactList).start();
    }





    public void setTimerByCall(View view) {
//        Intent intent=new Intent(MainActivity.this,TimerActivity.class);
       // intent.putExtra("Contact_name",personname);
//        intent.putExtra("Contact_numbers",contactnumbers);
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Call");
        startActivity(intent);
    }

    public void setTimerByMessage(View view) {
//        Intent intent=new Intent(MainActivity.this,TimerActivity.class);
        // intent.putExtra("Contact_name",personname);
//        intent.putExtra("Contact_numbers",contactnumbers);
        Intent intent= new Intent(MainActivity.this,DisplayContactListActivity.class);
        intent.putExtra("option","Message");
        startActivity(intent);
    }

    public void getVoice(View view) {
        Intent intent = new Intent(MainActivity.this,AudioRecorder.class);
        startActivity(intent);
    }
}