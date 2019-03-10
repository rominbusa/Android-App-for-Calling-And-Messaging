package com.example.callingandmessaging;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String msg;
    String number;
    public static CallTimerDatabase callTimerDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getStringExtra("option").equals("Call")) {
            setContentView(R.layout.activity_timer);
            callTimerDatabase = Room.databaseBuilder(getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();
        }
        else {
            setContentView(R.layout.activity_message_timer);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void settimer(View view) {

        //get time from time picker
        TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
        //tp.setIs24HourView(true);
        int startHour = tp.getHour();
        int startMinute = tp.getMinute();
        Log.d("Time is:",startHour+" "+startMinute);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,startMinute);
        calendar.set(Calendar.HOUR_OF_DAY,startHour);



        searchContact();

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if(getIntent().getStringExtra("option").equals("Call")) {
            Intent intent = new Intent(this, MyReceiver.class);
            Log.d("slected-name", getIntent().getStringExtra("Selected_name"));
            intent.putExtra("Selected_name",getIntent().getStringExtra("Selected_name"));
            intent.putExtra("number", number);
            int m = (int) System.currentTimeMillis() % 50000;

            SimpleDateFormat format = new SimpleDateFormat("h:mm a");
            Toast.makeText(this,format.format(calendar.getTime()),Toast.LENGTH_SHORT).show();

            CallTimeTable callTimeTable = new CallTimeTable();
            callTimeTable.setId(m);
            callTimeTable.setName(getIntent().getStringExtra("Selected_name"));
            callTimeTable.setTime(format.format(calendar.getTime()).toString());
            callTimeTable.setNumber(number);
            TimerActivity.callTimerDatabase.callDao().addTimer(callTimeTable);

            Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show();

            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        //for message
        else {
            EditText messageEditText = findViewById(R.id.editText);
            String messageText = messageEditText.getText().toString();

            Intent intent1 = new Intent(this,MessageActivity.class);
            //searchContact(getIntent().getStringExtra("Selected_name"));
            intent1.putExtra("number", number);
            intent1.putExtra("messageText",messageText);
            int m = (int) System.currentTimeMillis() % 50000;
            pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), m, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("msg",messageText);
        }
    }

    protected void searchContact(){
        String name = getIntent().getStringExtra("Selected_name");
        if(name == null){
            return;
        }
        ContactList contactList = (ContactList)getApplicationContext();
        ArrayList<Person> personArr = contactList.getPerson();

        Log.d("name is ",name);
        for(int i = 0;i<personArr.size();i++){
            if(name.toLowerCase().equals(personArr.get(i).getName().toLowerCase())){
                number = personArr.get(i).getContact_no()[0];
                Log.d("your selected name is",number);
                break;
            }
        }
    }
}
