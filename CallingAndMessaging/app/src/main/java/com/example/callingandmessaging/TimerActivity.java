package com.example.callingandmessaging;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String msg;
    String number;
    String dateStr;
    public static CallTimerDatabase callTimerDatabase;
    public static MessageTimerDatabase messageTimerDatabase;
    Calendar calendar;
    EditText editText,messageEditText;
    Button sendMessagebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();
        if(getIntent().getStringExtra("option").equals("Call")) {
            setContentView(R.layout.activity_timer);
            callTimerDatabase = Room.databaseBuilder(getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();
        }
        else {
            setContentView(R.layout.activity_message_timer);
            sendMessagebtn = findViewById(R.id.button3);
            sendMessagebtn.setEnabled(false);
            messageEditText = findViewById(R.id.editText);
            messageEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().trim().length()==0){
                        sendMessagebtn.setEnabled(false);
                    }else{
                        sendMessagebtn.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            messageTimerDatabase = Room.databaseBuilder(getApplicationContext(), MessageTimerDatabase.class, "MessageTimerdb").allowMainThreadQueries().build();
        }

        //fo date
        editText = (EditText)findViewById(R.id.date);
        Log.d("date", editText.getText().toString());
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                dateStr = String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("click", "onClick");
                // TODO Auto-generated method stub
                new DatePickerDialog(TimerActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void settimer(View view) {

        //calendar = Calendar.getInstance();
        //get time from time picker
        TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
        //tp.setIs24HourView(true);


        int startHour = tp.getHour();
        int startMinute = tp.getMinute();
        Log.d("Time is:",startHour+" "+startMinute);


        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,startMinute);
        calendar.set(Calendar.HOUR_OF_DAY,startHour);



        searchContact();

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat format = new SimpleDateFormat("h:mm a");
        Toast.makeText(this,format.format(calendar.getTime()),Toast.LENGTH_SHORT).show();

        if(getIntent().getStringExtra("option").equals("Call")) {
            Intent intent = new Intent(this, MyReceiver.class);
            Log.d("slected-name", getIntent().getStringExtra("Selected_name"));
            intent.putExtra("Selected_name",getIntent().getStringExtra("Selected_name"));
            intent.putExtra("number", number);
            int m = (int) System.currentTimeMillis() % 50000;
            Log.d("m:", String.valueOf(m));
            intent.putExtra("id", m);
//             intent.putExtra("requestCode", m);



            CallTimeTable callTimeTable = new CallTimeTable();
            callTimeTable.setId(m);
            callTimeTable.setName(getIntent().getStringExtra("Selected_name"));
            callTimeTable.setTime(format.format(calendar.getTime()).toString());
            callTimeTable.setNumber(number);
            callTimeTable.setDate(dateStr);
            TimerActivity.callTimerDatabase.callDao().addTimer(callTimeTable);

            Toast.makeText(this,"updated",Toast.LENGTH_SHORT).show();

            pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), m, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        //for message
        else {
//            messageEditText = findViewById(R.id.editText);
            String messageText = messageEditText.getText().toString();
            int m = (int) System.currentTimeMillis() % 50000;


            MessageTimeTable messageTimeTable = new MessageTimeTable();
            messageTimeTable.setId(m);
            messageTimeTable.setName(getIntent().getStringExtra("Selected_name"));
            messageTimeTable.setTime(format.format(calendar.getTime()).toString());
            messageTimeTable.setNumber(number);
            messageTimeTable.setMessageText(messageText);
            messageTimeTable.setDate(dateStr);
            TimerActivity.messageTimerDatabase.messageDao().addTimer(messageTimeTable);
            Toast.makeText(this, "Message Timer Added", Toast.LENGTH_SHORT).show();

            Intent intent1 = new Intent(this,MessageActivity.class);
            //searchContact(getIntent().getStringExtra("Selected_name"));
            intent1.putExtra("Timer", "Timer");
            intent1.putExtra("id", m);
            intent1.putExtra("number", number);
            intent1.putExtra("messageText",messageText);

            pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), m, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Log.d("msg",messageText);
        }

        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }



    //for date
    private void updateLabel()
    {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(calendar.getTime()));
    }

    protected void searchContact() {
        String name = getIntent().getStringExtra("Selected_name");
        if(name == null) {
            return;
        }
        ContactList contactList = (ContactList)getApplicationContext();
        ArrayList<Person> personArr = contactList.getPerson();

        Log.d("name is ",name);
        for(int i = 0;i<personArr.size();i++) {
            if(name.toLowerCase().equals(personArr.get(i).getName().toLowerCase())){
                number = personArr.get(i).getContact_no()[0];
                Log.d("your selected name is",number);
                break;
            }
        }
    }



}
