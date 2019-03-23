package com.example.callingandmessaging;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class userManual extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_manual);
        Log.d("in User manual","user manual inside");
    }
}
