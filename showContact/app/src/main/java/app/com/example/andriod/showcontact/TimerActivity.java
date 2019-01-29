package app.com.example.andriod.showcontact;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

public class TimerActivity extends AppCompatActivity {

    final static int START=0,STOP=1;

    StringBuilder s=new StringBuilder();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    //Context context = getBaseContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void button3_onClick(View view)
    {
        int startHour = Integer.parseInt(((EditText)findViewById(R.id.startHour)).getText().toString());
        int startMin = Integer.parseInt(((EditText)findViewById(R.id.startMin)).getText().toString());
        Log.d("time is ",startHour+"  "+startMin);
        s.append(startHour+" "+startMin+"\n");
        Log.d("all alarm are :\t",s.toString());
//        int stopHour = Integer.parseInt(((EditText)findViewById(R.id.stopHour)).getText().toString());
//        int stopMin = Integer.parseInt(((EditText)findViewById(R.id.stopMin)).getText().toString());
//
//        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        setAlarm(START,startHour,startMin);
//        setAlarm(STOP,stopHour,stopMin);

        alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent=new Intent(this,MyReceiver.class);
        int m=(int)System.currentTimeMillis()%50000;
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),m,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("alarm is set","lets see if it works");
        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,startMin);
        calendar.set(Calendar.HOUR_OF_DAY,startHour);

        long timeris=calendar.getTimeInMillis();
        Log.d("ago",String.valueOf(timeris) );
        //startActivity(intent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        long t =  System.currentTimeMillis();
        long c = calendar.getTimeInMillis();
        long diff=c-t;
        String s= "ashdkj "+t+"cal miils:"+c+"diff is  "+diff;
        Log.d("system time:",s);
        Log.d("alarm alarm","asd sfdb cv");
    }

    private void setAlarm(int type,int hour,int minute) {
        Intent intent = new Intent(this,MyReceiver.class);
        intent.putExtra("type",type);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,type,intent,PendingIntent.FLAG_ONE_SHOT);
        /*if(pIntent!=null)
        {
            Log.d("alarm","existing");
            alarmManager.cancel(pIntent);
        }*/
        //pIntent = PendingIntent.getBroadcast(this,type,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,minute);
        calendar.set(Calendar.HOUR_OF_DAY,hour);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

    }
}
