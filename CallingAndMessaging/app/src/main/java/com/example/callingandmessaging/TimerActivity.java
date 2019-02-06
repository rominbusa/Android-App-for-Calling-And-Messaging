package com.example.callingandmessaging;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {

    StringBuilder sb=new StringBuilder();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //Bundle b = getIntent().getExtras();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void settimer(View view) {

        //get time from time picker
        TimePicker tp=(TimePicker)findViewById(R.id.timePicker1);
        //tp.setIs24HourView(true);
        int startHour = tp.getHour();
        int startMinute=tp.getMinute();
        Log.d("Time is:",startHour+" "+startMinute);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,startMinute);
        calendar.set(Calendar.HOUR_OF_DAY,startHour);

        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent=new Intent(this,MyReceiver.class);
        int m=(int)System.currentTimeMillis()%50000;
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),m,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
    }
}
