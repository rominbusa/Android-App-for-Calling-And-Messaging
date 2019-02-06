package com.example.callingandmessaging;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ClearNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("check:","notification clear");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Bundle b = intent.getExtras();
        Log.d("n_id",String.valueOf(b.getInt("n_id")));
        notificationManager.cancel(b.getInt("n_id"));
    }
}
