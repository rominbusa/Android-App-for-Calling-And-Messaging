package com.example.callingandmessaging;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {MessageTimeTable.class},version = 1)
public abstract class MessageTimerDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();
}
