package com.example.callingandmessaging;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class TimerActivity extends AppCompatActivity {

    StringBuilder sb=new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Bundle b = getIntent().getExtras();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void settimer(View view) {
        TimePicker tp=(TimePicker)findViewById(R.id.timePicker1);

        //tp.setIs24HourView(true);
        //int startHour = tp.getHour();
        //int startMinute=tp.getMinute();
        //
        // Log.d("Time is:",startHour+" "+startMinute);
    }
}
