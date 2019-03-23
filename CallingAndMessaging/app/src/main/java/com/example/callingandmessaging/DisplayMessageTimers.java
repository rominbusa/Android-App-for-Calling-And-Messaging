package com.example.callingandmessaging;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.List;

public class DisplayMessageTimers extends Activity {
    public static MessageTimerDatabase messageTimerDatabase;

    private RecyclerView recyclerView;
    private MessageTimerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_message_timer);

        messageTimerDatabase = Room.databaseBuilder(getApplicationContext(), MessageTimerDatabase.class, "MessageTimerdb").allowMainThreadQueries().build();
        final List<MessageTimeTable> messageTimeTables = DisplayMessageTimers.messageTimerDatabase.messageDao().getAllTimers();
        messageTimerDatabase.close();
        //for recyclerview
        recyclerView = (RecyclerView) findViewById(R.id.messageTimerRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MessageTimerAdapter(messageTimeTables);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                messageTimerDatabase = Room.databaseBuilder(getApplicationContext(), MessageTimerDatabase.class, "MessageTimerdb").allowMainThreadQueries().build();
                DisplayMessageTimers.messageTimerDatabase.messageDao().deleteTimer(mAdapter.getMessageAt(viewHolder.getAdapterPosition()));
                messageTimeTables.remove(mAdapter.getMessageAt(viewHolder.getAdapterPosition()));
                mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                messageTimerDatabase.close();
                Toast.makeText(getApplicationContext(),"Message Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
    }
}
