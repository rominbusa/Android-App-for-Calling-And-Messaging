package app.com.example.andriod.showcontact;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getcontact(View view) {
        Intent intentContact=new Intent(MainActivity.this,ContactActivity.class);
        startActivity(intentContact);
    }

    public  void setTimer(View view)
    {
        Intent intent = new Intent(this,TimerActivity.class);
        startActivity(intent);
    }
}
