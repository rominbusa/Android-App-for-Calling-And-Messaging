package app.com.example.andriod.showcontact;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;


public class MyReceiver extends BroadcastReceiver {

    MediaPlayer mp;
    int i=0;

    @Override
    public void onReceive(Context context,Intent intent)
    {
        Log.d("works","success it works\n");
        Toast.makeText(context,"ALARM",Toast.LENGTH_LONG).show();

    }

}
