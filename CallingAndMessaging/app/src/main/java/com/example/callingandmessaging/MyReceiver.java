package com.example.callingandmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    private static CallTimerDatabase callTimerDatabase;
    private CallTimeTable callTimeTable;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("works","success it works\n");
//        Toast.makeText(context,"ALARM",Toast.LENGTH_LONG).show();

        callTimerDatabase = Room.databaseBuilder(context.getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();

        callTimeTable = MyReceiver.callTimerDatabase.callDao().getCallTimerById(intent.getIntExtra("id",-1));

        Log.d("m:", String.valueOf(intent.getIntExtra("id", 0)));

        if(callTimeTable == null)
        {
//            Toast.makeText(context, "it was Deleted", Toast.LENGTH_SHORT).show();
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent clearIntent = new Intent(context,ClearNotification.class);
        int notification_id = (int)System.currentTimeMillis()%20000;
        if(notification_id < 0){
            notification_id += 20000;
        }
        Log.d("id of intent is \t",String.valueOf(notification_id));

        Intent intent1 = new Intent(context,CallActivity.class);
        intent1.putExtra("number",intent.getStringExtra("number"));
        intent1.putExtra("n_id", notification_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("inmyreceiver",intent.getStringExtra("Selected_name"));

        clearIntent.putExtra("n_id",notification_id);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(context,notification_id,clearIntent,PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Call " + intent.getStringExtra("Selected_name"));
        builder.setContentText("Do you want to call?");
        builder.addAction(R.color.colorAccent,"MAKE CALL",pendingIntent);
        builder.addAction(R.color.colorAccent,"CANCEL",clearPendingIntent);

        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notification_id,builder.build());

        builder.setDeleteIntent(clearPendingIntent);
    }
}

