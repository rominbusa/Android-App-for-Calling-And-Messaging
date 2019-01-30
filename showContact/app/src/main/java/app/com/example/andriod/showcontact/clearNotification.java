package app.com.example.andriod.showcontact;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

//public class clearNotification extends Activity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Log.d("check:","notification clear");
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.cancel(1);
//    }
//}

public class clearNotification extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("check:","notification clear");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        //notificationManager.cancel(1);

        Bundle b = intent.getExtras();
        Log.d("n_id",String.valueOf(b.getInt("n_id")));
        notificationManager.cancel(b.getInt("n_id"));
    }
}