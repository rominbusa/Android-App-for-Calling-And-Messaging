package app.com.example.andriod.showcontact;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NotificationReceiver extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("yeah","yeah it works..");
        Toast.makeText(NotificationReceiver.this,"NotificationReceiverActivity",Toast.LENGTH_SHORT);
    }
}
