package com.example.callingandmessaging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class DisplayCallTimers extends Activity {

    public static CallTimerDatabase callTimerDatabase;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_call_timers);

        callTimerDatabase = Room.databaseBuilder(getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();

        List<CallTimeTable> callTimeTables = DisplayCallTimers.callTimerDatabase.callDao().gerCallTimers();

        //for recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.callTimerRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyCallTimerAdapter(callTimeTables);
        recyclerView.setAdapter(mAdapter);

        String items = "";
//        TextView item = (TextView) findViewById(R.id.items);
        for (CallTimeTable cs : callTimeTables)
        {
            items += cs.getName() + "\n" + cs.getNumber() + "\n" + cs.getTime() + "\n\n";
        }
        Log.d("callTimers:",items);
    }

    public void deleteCallTimer(View view)
    {
        view.findViewById(R.id.name);
    }
}
