package com.example.callingandmessaging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DisplayCallTimers extends Activity {

    public static CallTimerDatabase callTimerDatabase;

    private RecyclerView recyclerView;
    private MyCallTimerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_call_timers);

        callTimerDatabase = Room.databaseBuilder(getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();

        final List<CallTimeTable> callTimeTables = DisplayCallTimers.callTimerDatabase.callDao().gerCallTimers();
        callTimerDatabase.close();
        //for recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.callTimerRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyCallTimerAdapter(callTimeTables);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                callTimerDatabase = Room.databaseBuilder(getApplicationContext(),CallTimerDatabase.class,"CallTimerdb").allowMainThreadQueries().build();
                DisplayCallTimers.callTimerDatabase.callDao().deleteTimer(mAdapter.getCallTimerAt(viewHolder.getAdapterPosition()));
                callTimeTables.remove(mAdapter.getCallTimerAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                callTimerDatabase.close();
                Toast.makeText(getApplicationContext(), "swiped", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(mAdapter);
    }
}
